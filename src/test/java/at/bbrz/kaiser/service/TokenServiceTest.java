package at.bbrz.kaiser.service;

import at.bbrz.kaiser.exceptions.JWTValidationException;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    TokenService tokenService;

    @Mock
    JWTWrapper jwtWrapper;

    String username = "user";
    Instant expiryDate = Instant.parse("2025-03-14T08:56:23.793842400Z");
    String validToken = "token";


    @BeforeEach
    void setUp() {
        tokenService = new TokenService(jwtWrapper);
    }

    @Test
    void createToken_valid() {
        Mockito.when(jwtWrapper.createJWTToken(username, expiryDate)).thenReturn(validToken);
        String result = tokenService.createToken(username, expiryDate);
        assertEquals(validToken, result);
    }

    @Test
    void createToken_invalidParameters() {
        List<String> messages = List.of("Expiry date not valid when creating new JWT.", "Username not valid when creating new JWT.");
        JWTValidationException exception = assertThrows(JWTValidationException.class, () -> tokenService.createToken(null, null));
        assertTrue(exception.getMessages().containsAll(messages));

        messages = List.of("Username not valid when creating new JWT.");
        JWTValidationException usernameNullException = assertThrows(JWTValidationException.class, () -> tokenService.createToken(null, expiryDate));
        assertTrue(usernameNullException.getMessages().containsAll(messages));

        messages = List.of("Expiry date not valid when creating new JWT.");
        JWTValidationException expiryDateNullException = assertThrows(JWTValidationException.class, () -> tokenService.createToken(username, null));
        assertTrue(expiryDateNullException.getMessages().containsAll(messages));
    }

    @Test
    void createToken_shouldThrowValidationExcpetion() {
        Mockito.when(jwtWrapper.createJWTToken(username, expiryDate)).thenThrow(new JWTCreationException("Bla", null));
        JWTValidationException jwtValidationException = assertThrows(JWTValidationException.class, () -> {
            tokenService.createToken(username, expiryDate);
        });
        assertTrue(jwtValidationException.containsMessage("Bla"));
    }

}