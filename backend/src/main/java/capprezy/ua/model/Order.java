package capprezy.ua.model;

import capprezy.ua.model.other.PostgreSQLEnumType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.sql.Timestamp.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "_order")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    private Timestamp orderTime = new Timestamp(System.currentTimeMillis());
    private Timestamp readyTime;
    private Timestamp prefTime;
    private String preferences;
    private Double total = .0;
    private Double paid = .0;
    private String paymentId;

    @ManyToOne
    @JoinColumn(name = "clientUid")
    @JsonIgnoreProperties({"password", "role"})
    private AppUser client;

    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties({"order"})
    private List<Portion> portions = new ArrayList<>();


    public enum PaymentStateType {
        not_paid, fully_paid, part_paid
    }

    public enum OrderStateType {
        in_check, confirmed, in_progress, ready, took_away, cancelled
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "payment_state_type")
    @Type( type = "pgsql_enum" )
    private PaymentStateType paymentState = PaymentStateType.not_paid;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "order_state_type")
    @Type( type = "pgsql_enum" )
    private OrderStateType orderState = OrderStateType.in_check;;
}
