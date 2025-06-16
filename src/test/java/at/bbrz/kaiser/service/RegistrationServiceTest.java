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
    void couldRegisterWith_doesNotThrowWithUniqueUsername() {
        registrationService.couldRegisterWith(getValidUser());
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
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> registrationService.couldRegisterWith(userNullName));
        assertEquals("Username or Password is empty!", runtimeException.getMessage());
        RuntimeException runtimeException1 = assertThrows(RuntimeException.class, () -> registrationService.couldRegisterWith(userNullPassword));
        assertEquals("Username or Password is empty!", runtimeException1.getMessage());
    }

    @Test
    void couldRegisterWith_throwsExceptionIfUsernameIsTaken() {
        User user = User.builder()
                .name("taken")
                .password("password")
                .build();
        Mockito.when(userRepository.findByName("taken")).thenReturn(Optional.of(user));
        RuntimeException usernameTakenException = assertThrows(RuntimeException.class, () -> registrationService.couldRegisterWith(user));
        assertEquals("Username already taken!", usernameTakenException.getMessage());
    }

    @Test
    void couldRegisterWith_throwsExceptionIfPasswordIsInvalidLength() {
        User tooShort = User.builder()
                .name("user")
                .password("123")
                .build();
        User tooLong = User.builder()
                .name("user")
                .password("123456789abcdefghijklmnopqrstuvwxyz")
                .build();

        RuntimeException tooShortException = assertThrows(RuntimeException.class, () -> registrationService.couldRegisterWith(tooShort));
        assertEquals("Password length is invalid! (has to be between 7 and 32 characters)", tooShortException.getMessage());
        RuntimeException tooLongExcpetion = assertThrows(RuntimeException.class, () -> registrationService.couldRegisterWith(tooLong));
        assertEquals("Password length is invalid! (has to be between 7 and 32 characters)", tooLongExcpetion.getMessage());
    }

    @Test
    void couldRegisterWith_throwsExceptionIfUsernameIsTooLong() {
        User user = User.builder()
                .name("abcdefghijklmnopq")
                .password("password")
                .build();

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> registrationService.couldRegisterWith(user));
        assertEquals("Username is too long! (max 16 characters)", runtimeException.getMessage());
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