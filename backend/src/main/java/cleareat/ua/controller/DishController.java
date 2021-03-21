package capprezy.ua.controller;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.controller.exception.model.NotValidDataException;
import capprezy.ua.model.Category;
import capprezy.ua.model.Dish;
import capprezy.ua.model.dto.UploadDish;
import capprezy.ua.service.DishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("api/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping
    public ResponseEntity loadDishesPage(
            @PageableDefault(page = 0, size = 20)
            @SortDefault.SortDefaults({@SortDefault(sort = "name", direction = Sort.Direction.ASC)})
                    Pageable pageable,
            Integer[] categoryIn,
            Integer[] categoryNotIn,
            Integer[] ingredientIn,
            Integer[] ingredientNotIn,
            Double maxPrice,
            String like
    ) {
        List res = dishService.findByCriteria(categoryIn, categoryNotIn, ingredientIn, ingredientNotIn, maxPrice, like, pageable);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(dishService.findById(id));
    }

    @Component
    public static class StringToUserConverter implements Converter<String, Dish> {

        @Autowired
        private ObjectMapper objectMapper;

        @Override
        @SneakyThrows
        public Dish convert(String source) {
            return objectMapper.readValue(source, Dish.class);
        }
    }

    @PostMapping
    public ResponseEntity add(@RequestParam("file") MultipartFile file,
                              @RequestParam("dish") Dish dish) throws AlreadyExistsException, NotValidDataException {

        dish = dishService.add(dish);
        if (!file.isEmpty()) {
            try {
                dish = dishService.uploadPhotoToCloudinary(dish, file);
                dishService.update(dish);
            } catch (IOException e) {
                dishService.delete(dish.getUid());
                throw NotValidDataException.createWith("image uploading failed");
            }
        }
        return ResponseEntity.ok(dish);
    }


    @PutMapping
    public ResponseEntity edit(@RequestParam("dish") Dish dish,
                               @RequestParam(value = "file", required=false) MultipartFile file) throws NotValidDataException {
        if (file != null) {
            try {
                dish = dishService.uploadPhotoToCloudinary(dish, file);
                dishService.update(dish);
            } catch (IOException e) {
                throw NotValidDataException.createWith("image uploading failed");
            }
        }
        return ResponseEntity.ok(dishService.update(dish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        dishService.delete(id);
        return ResponseEntity.ok().build();
    }

}
