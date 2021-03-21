package capprezy.ua.repository;

import capprezy.ua.model.AppUser;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Integer> {

//    @Query(value = "SELECT u FROM _user u where u.mail = ?1 and u.password = ?2 ")
//    Optional<AppUser> login(String username, String password);
    Optional<AppUser> findByMail(String mail);
    Optional<AppUser> findByPhone(String phone);
}
