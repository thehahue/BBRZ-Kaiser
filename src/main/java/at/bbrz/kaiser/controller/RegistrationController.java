package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.model.RegistrationResponse;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerNewUser(@RequestBody User user) {
        try {
                registrationService.couldRegisterWith(user);
                registrationService.registerUser(user);
                return ResponseEntity.ok(buildResponse("User successfully registered!", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(buildResponse(e.getMessage(), false));
        }
    }

    private RegistrationResponse buildResponse(String message, Boolean success) {
        return RegistrationResponse.builder()
                .message(message)
                .success(success)
                .build();

    }
}
