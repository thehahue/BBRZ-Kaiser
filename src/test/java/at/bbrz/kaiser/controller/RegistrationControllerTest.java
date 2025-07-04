package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {UserService.class, RegistrationController.class})
@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    RegistrationController registrationController;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void registerNewUser_succeedsWithCode200() throws Exception {
        User user = getValidUser();
        Mockito.doNothing()
                .when(userService)
                .couldRegisterWith(user);
        Mockito.doNothing()
                .when(userService)
                .registerUser(user);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully registered!"));
    }

    @Test
    void registerNewUser_failsWithCode401_whenUsernameIsTaken() throws Exception {
        User userNameAlreadyTaken = getValidUser();

        Mockito.doThrow(new RuntimeException("Username already taken!"))
                .when(userService)
                .couldRegisterWith(Mockito.any(User.class));
        Mockito.doNothing()
                .when(userService)
                .registerUser(userNameAlreadyTaken);


        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userNameAlreadyTaken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username already taken!"));
    }




    private User getValidUser() {
        return User.builder()
                .name("validUser")
                .password("pass")
                .build();
    }
}