package at.bbrz.kaiser.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class JWTWrapperTest {
    JWTWrapper jwtWrapper = new JWTWrapper();
    String username = "user";
    Instant expiryDate = Instant.parse("2025-03-14T08:56:23.793842400Z");
    String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjE3NDE5NDI1ODN9.q7OZfffwwH_U0k2j6oI1__mTgOw2R6JxNzYTllfQK_Q";

    @BeforeEach
    void setUp() {
    }

    @Test
    void createJWTToken_valid() {
        String token = jwtWrapper.createJWTToken(username, expiryDate);
        assertEquals(validToken, token);
    }

    @Test
    void createJWTToken_invalid(){
        String token = jwtWrapper.createJWTToken(null, null);
        assertNotEquals(validToken, token);
    }

    @Test
    void createJWTToken_differentDate() {
        String token = jwtWrapper.createJWTToken(username, Instant.now());
        assertNotEquals(validToken, token);
    }
}