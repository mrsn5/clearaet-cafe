package capprezy.ua.repository;

import capprezy.ua.model.Category;
import capprezy.ua.model.Dish;
import capprezy.ua.model.DishIngredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishIngredientRepository extends CrudRepository<DishIngredient, Integer> {
    List<DishIngredient> findAllByDish(Dish dish);
    void deleteAllByDish(Dish dish);
}
