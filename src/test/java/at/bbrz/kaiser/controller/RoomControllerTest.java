package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.exceptions.RoomNotFoundException;
import at.bbrz.kaiser.model.RoomRequest;
import at.bbrz.kaiser.service.LoginService;
import at.bbrz.kaiser.service.RoomService;
import at.bbrz.kaiser.service.TokenService;
import at.bbrz.kaiser.service.UUIDWrapper;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { RoomService.class, RoomController.class, TokenService.class })
@WebMvcTest(RoomController.class)
class RoomControllerTest {

    RoomController roomController;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TokenService tokenService;

    @MockitoBean
    RoomService roomService;

    @MockitoBean
    UUIDWrapper uuidWrapper;

    @MockitoBean
    LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;


    String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjU3NDE5NDI1ODN9.0xkhKS8VKl-m4meKIEkd6-qXm_OZox1EkvWzNUTphLs";

    @BeforeEach
    void setUp() {
        roomController = new RoomController(roomService, tokenService, uuidWrapper, loginService);
    }

    @Test
    void getRoomDetailsWithValidTokenReturnsRoomResponse() throws Exception {
        Mockito.when(roomService.findNameById("roomid")).thenReturn("Room");
        Mockito.when(roomService.findUsersById("roomid")).thenReturn(new ArrayList<>());


        mockMvc.perform(get("/room/roomid").cookie(new Cookie("token", validToken)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":null,\"uuid\":\"roomid\",\"name\":\"Room\",\"users\":[]}"));

    }

    @Test
    void getRoomDetailsWithInvalidTokenReturnsUnauthorizedResponse() throws Exception {
        String token = "invalid";

        Mockito.doThrow(JWTVerificationException.class).when(tokenService).validateToken(token);

        mockMvc.perform(get("/room/roomid").cookie(new Cookie("token", token)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRoomDetailsWithInvalidRoomIdReturnsBadRequestResponse() throws Exception {
        String roomId = "wrong";

        Mockito.doThrow(RoomNotFoundException.class).when(roomService).findNameById(roomId);

        mockMvc.perform(get("/room/" + roomId).cookie(new Cookie("token", validToken)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewRoomWithInvalidTokenReturnsUnauthorizedResponse() throws Exception {
        String token = "invalid";

        Mockito.doThrow(JWTVerificationException.class).when(tokenService).validateToken(token);
        RoomRequest roomRequest = RoomRequest.builder()
                .name("Room")
                .build();
        mockMvc.perform(post("/room/createRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest))
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createNewRoomWithRoomRequestNullReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/room/createRoom")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("token", validToken)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createNewRoomWithRoomRequestNameNullReturnsBadRequest() throws Exception {
        RoomRequest roomRequest = RoomRequest.builder().build();

        mockMvc.perform(post("/room/createRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest))
                        .cookie(new Cookie("token", validToken)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRoomWithValidTokenReturnsRoomResponse() throws Exception {

        Mockito.when(uuidWrapper.createUUID()).thenReturn("roomId");
        Mockito.when(roomService.findNameById("roomid")).thenReturn("Room");
        Mockito.when(roomService.findUsersById("roomid")).thenReturn(new ArrayList<>());

        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("roomId")
                .name("Room")
                .build();

        mockMvc.perform(post("/room/createRoom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequest))
                        .cookie(new Cookie("token", validToken)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":null,\"uuid\":\"roomId\",\"name\":\"Room\",\"users\":null}"));
    }


}