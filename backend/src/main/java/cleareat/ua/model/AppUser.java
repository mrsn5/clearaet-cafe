package capprezy.ua.model;

import capprezy.ua.config.validator.Phone;
import capprezy.ua.model.other.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "_user")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;

    @Email @NotNull
    private String mail;

    @NotNull @NotEmpty
    private String fullName;

    @NotNull @NotEmpty @Phone
    private String phone;

    @NotNull @Size(min=8, message = "{password.length.min}") @Size(max = 73)
    private String password;

    public enum RoleType {
        admin, user
    }

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "role_type")
    @Type( type = "pgsql_enum" )
    private RoleType role = RoleType.user;
}

