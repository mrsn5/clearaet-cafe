package capprezy.ua.controller;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.model.Ingredient;
import capprezy.ua.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/ingredient")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping
    public ResponseEntity getAll() {
        return ResponseEntity.ok(ingredientService.getAll());
    }

    @PostMapping
    public ResponseEntity add(@RequestBody @Valid Ingredient ingredient) throws AlreadyExistsException {
        return ResponseEntity.ok(ingredientService.add(ingredient));
    }
}
