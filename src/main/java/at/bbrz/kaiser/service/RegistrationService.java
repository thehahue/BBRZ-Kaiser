package at.bbrz.kaiser.service;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    public boolean couldRegisterWith(User user) {
        if (user.getName().isEmpty() || user.getPassword().isEmpty()) {
            throw new RuntimeException("Username or Password is empty!");
        }
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }
        return true;
    }

    public void registerUser(User user) {
        userRepository.save(user);
    }
}
