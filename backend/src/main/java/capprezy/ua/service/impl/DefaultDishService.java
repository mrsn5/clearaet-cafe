package capprezy.ua.service.impl;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.model.*;
import capprezy.ua.repository.CategoryRepository;
import capprezy.ua.repository.DishIngredientRepository;
import capprezy.ua.repository.DishRepository;
import capprezy.ua.repository.IngredientRepository;
import capprezy.ua.service.DishService;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service("dishService")
public class DefaultDishService implements DishService {

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private DishIngredientRepository dishIngredientRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Value("${cloudinary.url}")
    Cloudinary cloudinary = new Cloudinary();

    @Override
    public List<Dish> findAll(Pageable pageable) {
        return dishRepository.findAll(pageable);
    }

    @Override
    public void delete(Integer id) {
        dishRepository.deleteById(id);
    }

    @Override
    public Dish findById(Integer id) {
        return dishRepository.findByUid(id);
    }

    @Override
    public Dish add(Dish dish) throws AlreadyExistsException {
        Dish d = dishRepository.findByNameIgnoreCase(dish.getName());
        if (d == null) {
//            List<Category> categories = new LinkedList<>();
//            for (Category c: dish.getCategories()) {
//                Category cat = categoryRepository.findByNameIgnoreCase(c.getName());
//                if (cat == null) {
//                    cat = categoryRepository.save(c);
//                }
//                categories.add(cat);
//            }
//            dish.setCategories(categories);


            Dish createdDish = dishRepository.save(dish);


            List<DishIngredient> dis = dish.getDishIngredients();
            for (DishIngredient di: dis) {
                di.setDish(createdDish);
                di.getId().setDishUid(createdDish.getUid());
                Ingredient ing = ingredientRepository.findByNameIgnoreCase(di.getIngredient().getName());
                if (ing == null) {
                    ing = ingredientRepository.save(di.getIngredient());
                }
                di.setIngredient(ing);
            }
            dishIngredientRepository.saveAll(dis);
            return createdDish;
        } else {
            throw AlreadyExistsException.createWith("This dish is already existed: " + d.getName());
        }
    }

    @Override
    public List<Dish> findByCriteria(Integer[] categoryIn,
                                     Integer[] categoryNotIn,
                                     Integer[] ingredientIn,
                                     Integer[] ingredientNotIn,
                                     Double maxPrice,
                                     String like,
                                     Pageable pageable) {
        return dishRepository.findByCriteria(
                categoryIn == null ? new ArrayList<>() : Arrays.asList(categoryIn),
                categoryNotIn == null ? new ArrayList<>() : Arrays.asList(categoryNotIn),
                ingredientIn == null ? new ArrayList<>() : Arrays.asList(ingredientIn),
                ingredientNotIn == null ? new ArrayList<>() : Arrays.asList(ingredientNotIn),
                maxPrice == null ? -1 : maxPrice,
                like == null ? "" : like.toLowerCase(),
                pageable);
    }

    public Dish uploadPhotoToCloudinary(Dish dish, MultipartFile toUpload) throws IOException {
        @SuppressWarnings("rawtypes")
        Map uploadResult = cloudinary.uploader().upload(toUpload.getBytes(), ObjectUtils.emptyMap());
        String cloudinaryUrl = (String) uploadResult.get("url");

        Dish oldDish = dishRepository.findByUid(dish.getUid());
        if (oldDish.getPhoto() != null) {
            String[] parts = oldDish.getPhoto().split("/");
            String[] names = parts[parts.length-1].split("[.]+");
            System.out.println(names[0]);
            cloudinary.uploader().destroy(names[0], ObjectUtils.emptyMap());
        }
        dish.setPhoto(cloudinaryUrl);
        return dish;
    }

    @Override
    public Dish update(Dish dish) {
        dishIngredientRepository.deleteAll(dishIngredientRepository.findAllByDish(dish));
        for (DishIngredient i: dish.getDishIngredients()) {
            if (i.getIngredient().getUid() == null) {
                Ingredient newIng = ingredientRepository.save(i.getIngredient());
                i.setIngredient(newIng);
            }
            i.setDish(dish);
            i.setId(new DishIngredientId(dish.getUid(), i.getIngredient().getUid()));
            dishIngredientRepository.save(i);
        }
        return dishRepository.save(dish);
    }

}
