package at.bbrz.kaiser.service;

import at.bbrz.kaiser.exceptions.JWTValidationException;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
@Service
public class TokenService {
    private JWTWrapper jwtWrapper;

    @Autowired
    public TokenService(JWTWrapper jwtWrapper) {
        this.jwtWrapper = jwtWrapper;
    }

    public String createToken(String userName, Instant expiryDate) {
        String jwt = "";
        JWTValidationException exception = new JWTValidationException();
        if (userName == null || userName.isEmpty()) {
            exception.addMessage("Username not valid when creating new JWT.");
        }
        if (expiryDate == null) {
            exception.addMessage("Expiry date not valid when creating new JWT.");
        }
        try {

            jwt = jwtWrapper.createJWTToken(userName, expiryDate);

        } catch (JWTCreationException jwtCreationException) {
            exception.addMessage(jwtCreationException.getMessage());
        }
        if (exception.shouldBeThrown()) {
            throw exception;
        }
        return jwt;
    }
}
