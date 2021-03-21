package capprezy.ua.service.impl;


import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.model.AppUser;
import capprezy.ua.repository.UserRepository;
import capprezy.ua.service.AppUserService;
import capprezy.ua.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service("userService")
public class DefaultUserService implements AppUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Override
    public AppUser register(AppUser appUser) throws AlreadyExistsException {

        Optional<AppUser> _user = userRepository.findByMail(appUser.getMail());
        if (_user.isEmpty() /* &&_user2.isEmpty()*/) {
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            appUser = userRepository.save(appUser);
            mailService.sendGreeting(appUser);
            return appUser;
        } else if (_user.isPresent()) {
            throw AlreadyExistsException.createWith("This mail is already in system");
        }
        return null;
    }

    @Override
    public User loadUserByUsername(String username) {
        Optional<AppUser> _appUser = userRepository.findByMail(username);
        if(_appUser.isPresent()){
            AppUser appUser = _appUser.get();

            List<GrantedAuthority> ga = AuthorityUtils.createAuthorityList(appUser.getRole() == AppUser.RoleType.admin ? "ADMIN" : "USER");
            return new User(appUser.getMail(), appUser.getPassword(), true, true, true, true,
                    ga);
        }
        return null;
    }

    @Override
    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<AppUser> user = userRepository.findByMail(authentication.getName());
        return user.orElse(null);
    }

    public AppUser findById(Integer id) {
        Optional<AppUser> customer= userRepository.findById(id);
        return customer.orElse(null);
    }

    @Override
    public AppUser findByMail(String mail) {
        Optional<AppUser> customer= userRepository.findByMail(mail);
        return customer.orElse(null);
    }
}
