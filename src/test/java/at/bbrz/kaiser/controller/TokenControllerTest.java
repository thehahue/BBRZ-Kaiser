package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.model.PayloadResponse;
import at.bbrz.kaiser.service.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { TokenService.class, TokenController.class })
@WebMvcTest(TokenController.class)
class TokenControllerTest {
    private TokenController tokenController;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenController = new TokenController(tokenService);
    }

//    @Test
//    void secureEndpointReturnsHeaderWithValidToken() {
//        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjE3NDE5NDI1ODN9.q7OZfffwwH_U0k2j6oI1__mTgOw2R6JxNzYTllfQK_Q";
//        ResponseEntity<String> response = tokenController.secureEndpoint("Bearer " + validToken);
//        assertEquals(ResponseEntity.ok("{\"status\":\"verified\"}"), response);
//    }

    @Test
    void secureEndpoint_ValidToken() throws Exception {
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjU3NDE5NDI1ODN9.0xkhKS8VKl-m4meKIEkd6-qXm_OZox1EkvWzNUTphLs";

        mockMvc.perform(post("/secureTest")
                .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"status\":\"verified\"}"));
    }

    @Test
    void secureEndpoint_InvalidToken() throws Exception {
        String invalidToken = "invalid";

        Mockito.doThrow(JWTVerificationException.class).when(tokenService).validateToken(invalidToken);

        mockMvc.perform(post("/secureTest")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("{\"status\":\"not verified\"}"));
    }

    @Test
    void secureEndpoint_MissingHeader() throws Exception{
        mockMvc.perform(post("/secureTest"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void secureEndpoint_InvalidHeaderFormat() throws Exception{
        mockMvc.perform(post("/secureTest")
                .header("Authorization", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void decodeUserReturns_CorrectResponse() {
        Mockito.when(tokenService.getUserNameFromToken("validToken")).thenReturn("admin");
        ResponseEntity<PayloadResponse> payloadResponseResponseEntity = tokenController.decodeUsernameFromPayload("validToken");
        assertEquals("admin", payloadResponseResponseEntity.getBody().getUsername());
    }


}