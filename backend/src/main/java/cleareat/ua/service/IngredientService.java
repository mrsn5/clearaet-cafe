package capprezy.ua.service;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.model.Ingredient;

import java.util.List;

public interface IngredientService {
    List<Ingredient> getAll();
    Ingredient add(Ingredient ingredient) throws AlreadyExistsException;
}
