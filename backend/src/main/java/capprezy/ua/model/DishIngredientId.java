package capprezy.ua.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class DishIngredientId implements Serializable {
    @Column(name = "dishUid")
    private Integer dishUid;
    @Column(name = "ingredientUid")
    private Integer ingredientUid;
}
