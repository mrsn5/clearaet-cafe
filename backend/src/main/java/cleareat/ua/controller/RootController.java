package capprezy.ua.controller;

import capprezy.ua.config.security.jwt.JwtRequest;
import capprezy.ua.config.security.jwt.JwtTokenUtil;
import capprezy.ua.controller.exception.model.AlreadyExistsException;
import capprezy.ua.controller.exception.model.NotValidDataException;
import capprezy.ua.model.AppUser;
import capprezy.ua.model.Payment;
import capprezy.ua.model.dto.AppUserWithToken;
import capprezy.ua.service.AppUserService;
import capprezy.ua.service.OrderService;
import capprezy.ua.service.impl.LiqpayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.liqpay.LiqPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/")
@CrossOrigin
public class RootController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private LiqpayService liqpayService;

    @GetMapping("/")
    public RedirectView redirectWithUsingRedirectView(
            RedirectAttributes attributes) {
//        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
//        attributes.addAttribute("attribute", "redirectWithRedirectView");
        return new RedirectView("index.html");
    }

    @PostMapping("api/register")
    public ResponseEntity<AppUser> registerUser(@RequestBody @Valid AppUser appUser) throws AlreadyExistsException {
        System.out.println(appUser.toString());
        return ResponseEntity.ok(appUserService.register(appUser));
    }

    @PostMapping(value = "api/authenticate")
    public ResponseEntity<AppUserWithToken> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = appUserService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final AppUser user = appUserService.findByMail(authenticationRequest.getUsername());
        return ResponseEntity.ok(new AppUserWithToken(user, token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping(value = "api/payment", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity acceptPayment(@RequestParam HashMap<String, Object> params) throws NotValidDataException, JsonProcessingException {
//        System.out.println(params);
        liqpayService.checkPayment(params);
        return ResponseEntity.ok().build();
    }
}
