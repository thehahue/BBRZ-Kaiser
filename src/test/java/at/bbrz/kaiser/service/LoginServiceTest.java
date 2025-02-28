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

    @Test
    void loginShouldFail_WithEmptyStrings() {
        boolean canLogin = canLogin("", "");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldBeCaseSensitive() {
        boolean canLogin = canLogin("Dave", "pass");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithExtraSpaces() {
        boolean canLogin = canLogin(" dave", "pass");
        assertFalse(canLogin);

        canLogin = canLogin("dave ", "pass");
        assertFalse(canLogin);

        canLogin = canLogin("dave", " pass");
        assertFalse(canLogin);

        canLogin = canLogin("dave", "pass ");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithOnlyOneGivenParameter() {
        boolean canLogin = canLogin("dave", null);
        assertFalse(canLogin);

        canLogin = canLogin(null, "pass");
        assertFalse(canLogin);
    }

    private boolean canLogin(String user, String password) {
        return loginService.couldLoginWith(user, password);
    }
}