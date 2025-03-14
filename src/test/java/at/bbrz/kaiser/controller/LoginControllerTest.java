package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.service.JWTWrapper;
import at.bbrz.kaiser.service.LoginService;
import at.bbrz.kaiser.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { LoginController.class, TokenService.class, JWTWrapper.class })
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    public static final String TOKEN = "token";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testTryLogin_succeeds() throws Exception {
        Mockito.when(loginService.couldLoginWith("testuser", "password")).thenReturn(true);

        MvcResult mvcResult = mockMvc.perform(post("/test")
                        .param("name", "testuser")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();

        String[] splitToken = mvcResult.getResponse().getContentAsString().split("\\.");
        assertEquals(3, splitToken.length);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", splitToken[0]);
        assertTrue(splitToken[1].length() > 5);
        assertTrue(splitToken[2].length() > 5);
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

        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn();

        String[] splitToken = mvcResult.getResponse().getContentAsString().split("\\.");
        assertEquals(3, splitToken.length);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", splitToken[0]);
        assertTrue(splitToken[1].length() > 5);
        assertTrue(splitToken[2].length() > 5);
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