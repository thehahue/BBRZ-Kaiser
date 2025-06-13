package at.bbrz.kaiser.service;

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
class RegistrationServiceTest {

    @InjectMocks
    RegistrationService registrationService;

    @Mock
    UserRepository userRepository;

    @Test
    void couldRegisterWith_returnsTrueWithUniqueUsername() {
        assertTrue(registrationService.couldRegisterWith(getValidUser()));
    }

    @Test
    void couldRegisterWith_throwsExceptionIfUserNameOrPasswordIsEmpty() {
        User userNullName = User.builder()
                .name("")
                .password("pass")
                .build();
        User userNullPassword = User.builder()
                .name("name")
                .password("")
                .build();
        assertThrows(RuntimeException.class, () -> {registrationService.couldRegisterWith(userNullName);});
        assertThrows(RuntimeException.class, () -> {registrationService.couldRegisterWith(userNullPassword);});
    }

    @Test
    void couldRegisterWith_throwsExceptionIfUsernameIsTaken() {
        User user = getValidUser();
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> {
            registrationService.couldRegisterWith(user);
        });
    }

    @Test
    void registerUser_SavesUser() {
        User user = getValidUser();
        registrationService.registerUser(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    private User getValidUser() {
        return User.builder()
                .name("ValidUser")
                .password("validPass")
                .build();
    }

}