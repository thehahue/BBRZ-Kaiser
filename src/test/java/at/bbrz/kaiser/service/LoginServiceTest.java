package at.bbrz.kaiser.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    private LoginService loginService;


    @BeforeEach
    void setUp() {
        loginService = new LoginService();
    }

    @Test
    void loginShouldFail_WithWrongUserOrPassword() {
        boolean canLogin = canLogin("da", "pa");
        assertFalse(canLogin);
    }

    @Test
    void loginSuccess_WithCorrectUserAndPassword() {
        boolean canLogin = canLogin("dave", "pass");
        assertTrue(canLogin);
    }

    @Test
    void loginShouldFail_WithNullValues() {
        boolean canLogin = canLogin(null, null);
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithCorrectUserAndWrongPassword() {
        boolean canLogin = canLogin("dave", "pas");
        assertFalse(canLogin);
    }

    private boolean canLogin(String user, String password) {
        return loginService.couldLoginWith(user, password);
    }
}