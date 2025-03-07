package at.bbrz.kaiser.service;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {

    }

    private void mockUserList() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(
                User.builder()
                        .name("admin")
                        .password("password").build()
                , User.builder()
                        .name("user")
                        .password("password")
                        .build()));
    }

    @Test
    void loginShouldFail_WithWrongUserOrPassword() {
        mockUserList();
        boolean canLogin = canLogin("da", "pa");
        assertFalse(canLogin);
    }

    @Test
    void loginSuccess_WithCorrectUserAndPassword() {
        mockUserList();
        boolean canLogin = canLogin("user", "password");
        assertTrue(canLogin);
    }

    @Test
    void loginShouldFail_WithNullValues() {
        boolean canLogin = canLogin(null, null);
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithCorrectUserAndWrongPassword() {
        mockUserList();
        boolean canLogin = canLogin("dave", "pas");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithEmptyStrings() {
        mockUserList();
        boolean canLogin = canLogin("", "");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldBeCaseSensitive() {
        mockUserList();
        boolean canLogin = canLogin("Dave", "pass");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithExtraSpaces() {
        mockUserList();
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