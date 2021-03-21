package capprezy.ua.service.impl;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.model.Ingredient;
import capprezy.ua.repository.IngredientRepository;
import capprezy.ua.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ingredientService")
public class DefaultIngredientService implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient add(Ingredient ingredient) throws AlreadyExistsException {
        Ingredient i = ingredientRepository.findByNameIgnoreCase(ingredient.getName());
        if (i == null) {
            return ingredientRepository.save(ingredient);
        } else {
            throw AlreadyExistsException.createWith("This ingredient is already existed: " + i.getName());
        }
    }
}
