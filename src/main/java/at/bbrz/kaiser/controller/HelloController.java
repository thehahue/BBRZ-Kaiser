package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.service.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    TokenService tokenService;

    public HelloController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @PostMapping("/secureTest")
    public ResponseEntity<String> secureEndpoint(@RequestHeader("Authorization") String authHeader) {
        String[] authHeaderSplit = authHeader.split(" ");
        if (authHeaderSplit.length != 2 || !authHeaderSplit[0].equals("Bearer")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Header");
        }
        try {
            tokenService.validateToken(authHeaderSplit[1], "user");
            return ResponseEntity.ok("Verified");
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not Verified");
        }
    }
}
