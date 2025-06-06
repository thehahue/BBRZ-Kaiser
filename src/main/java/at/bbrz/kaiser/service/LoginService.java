package at.bbrz.kaiser.service;

import at.bbrz.kaiser.exceptions.UserNotFoundException;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    public boolean couldLoginWith(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        User user = userRepository.findByNameAndPassword(username, password);

        return user != null;

    }

    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByName(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        return user.get();
    }

}
