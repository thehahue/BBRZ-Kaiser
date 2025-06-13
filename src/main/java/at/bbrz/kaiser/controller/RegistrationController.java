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
            if (registrationService.couldRegisterWith(user)) {
                registrationService.registerUser(user);
                return ResponseEntity.ok(RegistrationResponse.builder()
                        .message("User sucessfully registered!")
                        .success(true)
                        .build());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(RegistrationResponse.builder()
                            .message(e.getMessage())
                            .success(false)
                            .build());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RegistrationResponse.builder()
                        .message("Something went wrong!")
                        .success(false)
                        .build());
    }
}
