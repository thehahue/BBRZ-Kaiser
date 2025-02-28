package at.bbrz.kaiser;

import at.bbrz.kaiser.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class LoginController {

    private final LoginService loginService = new LoginService();


    @GetMapping("/login?name={name}&pw={password}")
    public ResponseEntity<String> tryLogin(@PathVariable String name, @PathVariable String password) {
        if (loginService.couldLoginWith(name, password)) {
            return ResponseEntity.ok("OK");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> tryLoginFromJson(@RequestBody User user) {
        if (loginService.couldLoginWith(user.getName(), user.getPassword())) {
            return ResponseEntity.ok("OK");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
    }
}
