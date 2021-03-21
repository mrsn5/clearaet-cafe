package capprezy.ua.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "_dish")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    @NotEmpty(message = "{name.empty}") @NotNull(message = "{name.empty}")
    private String name;

    @NotNull
    private Double weight;

    @NotNull
    private Double price;

    private Integer calories = 0;
    private Boolean isAvailable = true;
    private String photo;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "_dish_category",
        joinColumns = @JoinColumn(name = "dish_uid"),
        inverseJoinColumns = @JoinColumn(name = "category_uid"))
    private Collection<Category> categories;

    @OneToMany(mappedBy = "dish")
    @JsonIgnoreProperties({"dish", "id"})
    private List<DishIngredient> dishIngredients = new ArrayList<>();
}
