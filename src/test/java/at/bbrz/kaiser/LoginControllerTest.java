package at.bbrz.kaiser;

import at.bbrz.kaiser.controller.LoginController;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testTryLogin_succeeds() throws Exception {
        Mockito.when(loginService.couldLoginWith("testuser", "password")).thenReturn(true);

        mockMvc.perform(post("/test")
                        .param("name", "testuser")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }


    @Test
    void testTryLogin_fails() throws Exception {
        Mockito.when(loginService.couldLoginWith("wronguser", "wrongpass")).thenReturn(false);

        mockMvc.perform(post("/test")
                        .param("name", "wronguser")
                        .param("password", "wrongpass"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Login failed!"));
    }

    @Test
    void testTryLoginFromJson_succeeds() throws Exception {
        User user = new User("testuser", "password");
        Mockito.when(loginService.couldLoginWith("testuser", "password")).thenReturn(true);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void testTryLoginFromJson_fails() throws Exception {
        User user = new User("testuser", "password");
        Mockito.when(loginService.couldLoginWith("testuser", "password")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Login failed!"));
    }
}