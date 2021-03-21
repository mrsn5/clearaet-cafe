package capprezy.ua.controller;

import capprezy.ua.model.AppUser;
import capprezy.ua.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class AppUserController {

    @Autowired
    private AppUserService userService;

    @GetMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<AppUser> getUserDetail(@PathVariable Integer id){
        return ResponseEntity.ok(userService.findById(id));
    }


}
