package capprezy.ua.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "_dish_ingredient")
public class DishIngredient {

    @EmbeddedId
    private DishIngredientId id = new DishIngredientId();

    private Double quantity;

    @ManyToOne
    @MapsId("dishUid")
    @JsonIgnoreProperties("dishIngredients")
    private Dish dish;

    @ManyToOne
    @MapsId("ingredientUid")
    private Ingredient ingredient;
}

