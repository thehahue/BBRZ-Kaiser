package at.bbrz.kaiser.service;

import at.bbrz.kaiser.exceptions.JWTValidationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
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

    public void validateToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256("Kaiser00000000000000000000000000");
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }

    public String getUserNameFromToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        if (jwt.getClaim("username").isNull()) {
            throw new JWTDecodeException("Invalid Token");
        }
        return jwt.getClaim("username").asString();
    }
}
