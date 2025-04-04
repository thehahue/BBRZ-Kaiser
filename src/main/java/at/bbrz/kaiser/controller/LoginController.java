package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.model.LoginResponse;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.service.LoginService;
import at.bbrz.kaiser.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;


@RestController
public class LoginController {

    @Autowired
    private final LoginService loginService;

    @Autowired
    private TokenService tokenService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> tryLogin(@RequestBody User user) {
        if (loginService.couldLoginWith(user.getName(), user.getPassword())) {
            LoginResponse response = LoginResponse.builder()
                    .token(tokenService.createToken(user.getName(), Instant.now().plusSeconds(300)))
                    .message("Login Success")
                    .success(true)
                    .path("/success.html")
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(LoginResponse.builder()
                .message("Login failed")
                .success(false)
                .build());
    }
}
