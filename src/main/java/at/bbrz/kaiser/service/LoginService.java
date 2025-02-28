package at.bbrz.kaiser.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginService {
    private final Map<String, String> loginData = Map.of(
            "dave", "pass"
    );
    public boolean couldLoginWith(String user, String password) {
        if (user == null || password == null) {
            return false;
        }
        return loginData.containsKey(user) && loginData.get(user).equals(password);

    }

}
