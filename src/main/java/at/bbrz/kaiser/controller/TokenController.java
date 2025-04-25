package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.model.PayloadResponse;
import at.bbrz.kaiser.service.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log
@RestController
public class TokenController {
    private TokenService tokenService;

    @Autowired
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/secureTest")
    public ResponseEntity<String> secureEndpoint() {
        return ResponseEntity.ok("{\"status\":\"verified\"}");
    }


    @GetMapping("/decodePayload")
    public ResponseEntity<PayloadResponse> decodeUsernameFromPayload(@CookieValue(value = "token") String token) {
        String username = tokenService.getUserNameFromToken(token);
        PayloadResponse response = PayloadResponse.builder()
                .username(username)
                .build();
        return ResponseEntity.ok(response);
    }
}
