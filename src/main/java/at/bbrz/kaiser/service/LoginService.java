package at.bbrz.kaiser.service;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    public boolean couldLoginWith(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        List<User> users = userRepository.findAll();
        User user = users.stream().filter(u -> u.getName().equals(username) && u.getPassword().equals(password)).findAny().orElse(null);
        return user != null;

    }

}
