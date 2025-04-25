package at.bbrz.kaiser.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JWTWrapper {
    public String createJWTToken(String userName, Instant expiryDate) throws JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256("Kaiser");
        return JWT.create()
                .withHeader("""
                            {
                              "alg": "HS256"
                            }""")
                .withClaim("username", userName)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }
}
