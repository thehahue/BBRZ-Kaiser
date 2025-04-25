package at.bbrz.kaiser.service;

import at.bbrz.kaiser.exceptions.JWTValidationException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
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
    void createToken_shouldThrowValidationException() {
        Mockito.when(jwtWrapper.createJWTToken(username, expiryDate)).thenThrow(new JWTCreationException("Bla", null));
        JWTValidationException jwtValidationException = assertThrows(JWTValidationException.class, () -> {
            tokenService.createToken(username, expiryDate);
        });
        assertTrue(jwtValidationException.containsMessage("Bla"));
    }

    @Test
    void validateTokenDoesNotThrowException() {
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiZXhwIjoxOTc0NTU2ODIxOX0.94aAqEy_b4lr4phucHQ2-0k0WeojJvczKKZCD4YphiY";
        tokenService.validateToken(validToken);
    }

    @Test
    void validateTokenThrowsExceptionWithInvalidToken() {
        assertThrows(JWTVerificationException.class, () -> {
            tokenService.validateToken("test");
        });
    }

    @Test
    void validateToken_failsWithExpiredDate() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjE3NDE5NDI1ODN9.q7OZfffwwH_U0k2j6oI1__mTgOw2R6JxNzYTllfQK_Q";
        assertThrows(JWTVerificationException.class, () -> {
            tokenService.validateToken(invalidToken);
        });
    }


    @Test
    void validateToken_failsWithIncorrectSignature() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjU3NDE5NDI1ODN9.qoXXnVlkQh4suvSL1EE-C1d7tzhxfUCc5obOn3uj-bE";
        assertThrows(JWTVerificationException.class, () -> {
            tokenService.validateToken(invalidToken);
        });
    }

    @Test
    void getUserNameFromToken_returnsUsername() {
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjU3NDE5NDI1ODN9.0xkhKS8VKl-m4meKIEkd6-qXm_OZox1EkvWzNUTphLs";
        String userNameFromToken = tokenService.getUserNameFromToken(validToken);
        assertEquals("user", userNameFromToken);
    }

    @Test
    void getUserNameFromToken_throwsException() {
        String tokenWithNullUsername = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6bnVsbCwiZXhwIjoxNzQzNzY0MDQ0fQ.wqMX61nYap_RuBlz-L8lK6QkHUuc4oIoIqqpwpJ20RE";
        assertThrows(JWTDecodeException.class, () -> tokenService.getUserNameFromToken(tokenWithNullUsername));
    }
}