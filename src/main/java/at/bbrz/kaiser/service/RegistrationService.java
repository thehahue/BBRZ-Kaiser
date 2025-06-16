package at.bbrz.kaiser.service;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    public void couldRegisterWith(User user) {
        String name = user.getName().trim();
        String password = user.getPassword().trim();
        if (name.isEmpty() || password.isEmpty()) {
            throw new RuntimeException("Username or Password is empty!");
        }
        if (userRepository.findByName(name.toLowerCase()).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }
        if (password.length() <= 6 || password.length() > 32) {
            throw new RuntimeException("Password length is invalid! (has to be between 7 and 32 characters)");
        }
        if (name.length() > 16) {
            throw new RuntimeException("Username is too long! (max 16 characters)");
        }
    }

    public void registerUser(User user) {
        userRepository.save(user);
    }
}
