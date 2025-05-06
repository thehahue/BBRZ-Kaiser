package at.bbrz.kaiser.service;

import at.bbrz.kaiser.exceptions.UserNotFoundException;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Test
    void loginShouldFail_WithWrongUserOrPassword() {
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
        boolean canLogin = canLogin("user", "pas");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithEmptyStrings() {
        boolean canLogin = canLogin("", "");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldBeCaseSensitive() {
        boolean canLogin = canLogin("User", "password");
        assertFalse(canLogin);
    }

    @Test
    void loginShouldFail_WithExtraSpaces() {
        loginSuccess_WithCorrectUserAndPassword();

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

    @Test
    void findByUsername_shouldReturnUser_whenUserExists() {
        String username = "testuser";
        User user = User.builder()
                .name(username)
                .build();

        Mockito.when(userRepository.findByName(username)).thenReturn(Optional.ofNullable(user));

        User result = loginService.findByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getName());
    }

    @Test
    void findByUsername_shouldThrowException_whenUserNotFound() {
        String username = "unknown";
        Mockito.when(userRepository.findByName(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> loginService.findByUsername(username));
    }

    private boolean canLogin(String user, String password) {
        return loginService.couldLoginWith(user, password);
    }

}