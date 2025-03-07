package at.bbrz.kaiser.service;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loginShouldFail_WithWrongUserOrPassword() {
        Mockito.when(userRepository.findByNameAndPassword("da", "pa")).thenReturn(null);
        boolean canLogin = canLogin("da", "pa");
        assertFalse(canLogin);
    }

    @Test
    void loginSuccess_WithCorrectUserAndPassword() {
        Mockito.when(userRepository.findByNameAndPassword("user", "password")).thenReturn(User.builder()
                .name("user")
                .password("password").build());
        boolean canLogin = canLogin("user", "password");
        assertTrue(canLogin);
    }

    @Test
    void loginShouldFail_WithNullValues() {
        boolean canLogin = canLogin(null, null);
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithCorrectUsernameAndWrongPassword() {
        Mockito.when(userRepository.findByNameAndPassword("user", "pas")).thenReturn(null);
        boolean canLogin = canLogin("user", "pas");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithEmptyStrings() {
        Mockito.when(userRepository.findByNameAndPassword("", "")).thenReturn(null);
        boolean canLogin = canLogin("", "");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldBeCaseSensitive() {
        Mockito.when(userRepository.findByNameAndPassword("User", "password")).thenReturn(null);
        boolean canLogin = canLogin("User", "password");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithExtraSpaces() {
        Mockito.when(userRepository.findByNameAndPassword(" user", "password")).thenReturn(null);
        Mockito.when(userRepository.findByNameAndPassword("user ", "password")).thenReturn(null);
        Mockito.when(userRepository.findByNameAndPassword("user", " password")).thenReturn(null);
        Mockito.when(userRepository.findByNameAndPassword("user", "password ")).thenReturn(null);

        boolean canLogin = canLogin(" user", "password");
        assertFalse(canLogin);

        canLogin = canLogin("user ", "password");
        assertFalse(canLogin);

        canLogin = canLogin("user", " password");
        assertFalse(canLogin);

        canLogin = canLogin("user", "password ");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithOnlyOneGivenParameter() {
        boolean canLogin = canLogin("user", null);
        assertFalse(canLogin);

        canLogin = canLogin(null, "password");
        assertFalse(canLogin);
    }

    private boolean canLogin(String user, String password) {
        return loginService.couldLoginWith(user, password);
    }

}