package capprezy.ua.model.dto;

import capprezy.ua.model.AppUser;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class AppUserWithToken extends AppUser {

    private String token;

    public AppUserWithToken(AppUser user, String token) {
        super(user.getUid(), user.getMail(), user.getFullName(), user.getPhone(), user.getPassword(), user.getRole());
        this.token = token;
    }
}
