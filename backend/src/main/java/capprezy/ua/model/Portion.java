package capprezy.ua.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.DataOutputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "_portion")
public class Portion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    Boolean isCancelled = false;

    @ManyToOne
    @JoinColumn(name = "orderUid")
    @JsonIgnoreProperties("portions")
    private Order order;


    @ManyToOne
    @JoinColumn(name = "dishUid")
    @JsonIgnoreProperties({"categories", "dishIngredients"})
    private Dish dish;

    private Integer quantity = 1;
    private Double price;


}
