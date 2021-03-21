package capprezy.ua.service;

import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.model.AppUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    AppUser register(AppUser appUser) throws AlreadyExistsException;
    AppUser findById(Integer id);
    AppUser findByMail(String mail);
    User loadUserByUsername(String username);
    AppUser getCurrentUser();
}
