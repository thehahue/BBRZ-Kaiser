package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.exceptions.RoomNotFoundException;
import at.bbrz.kaiser.exceptions.UserNotFoundException;
import at.bbrz.kaiser.model.Room;
import at.bbrz.kaiser.model.RoomRequest;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.service.*;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {RoomService.class, RoomController.class, TokenService.class})
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

    @MockitoBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIiLCJleHAiOjU3NDE5NDI1ODN9.0xkhKS8VKl-m4meKIEkd6-qXm_OZox1EkvWzNUTphLs";

    @BeforeEach
    void setUp() {
        roomController = new RoomController(roomService, tokenService, uuidWrapper, loginService, userService);
    }

    @Test
    void getRoomDetailsWithValidTokenReturnsValidRoomResponse() throws Exception {
        Mockito.when(roomService.findNameById("roomid")).thenReturn("Room");
        Mockito.when(roomService.findUsersById("roomid")).thenReturn(new ArrayList<>());


        mockMvc.perform(get("/room/info/roomid").cookie(new Cookie("token", validToken)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":null,\"uuid\":\"roomid\",\"name\":\"Room\",\"users\":[]}"));

    }

    @Test
    void getRoomDetailsWithInvalidTokenReturnsUnauthorizedResponse() throws Exception {
        String token = "invalid";

        Mockito.doThrow(JWTVerificationException.class).when(tokenService).validateToken(token);

        mockMvc.perform(get("/room/info/roomid").cookie(new Cookie("token", token)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getRoomDetailsWithInvalidRoomIdReturnsBadRequestResponse() throws Exception {
        String roomId = "wrong";

        Mockito.doThrow(RoomNotFoundException.class).when(roomService).findNameById(roomId);

        mockMvc.perform(get("/room/info/" + roomId).cookie(new Cookie("token", validToken)))
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

    @Test
    void joinRoomThatExistsWithValidToken() throws Exception {
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("123")
                .build();
        Room room = Room.builder()
                .name("TestRoom")
                .uuid("123")
                .users(new ArrayList<>())
                .build();
        User user = User.builder()
                .name("TestUser")
                .password("test")
                .build();

        Mockito.when(tokenService.getUserNameFromToken(validToken)).thenReturn(user.getName());
        Mockito.when(roomService.findRoomById(room.getUuid())).thenReturn(room);
        Mockito.when(loginService.findByUsername(user.getName())).thenReturn(user);

        mockMvc.perform((post("/room/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", validToken))
                        .content(objectMapper.writeValueAsString(roomRequest))))
                .andExpect(status().isOk());

    }

    @Test
    void joinRoom_shouldReturnUnauthorized_whenTokenInvalid() throws Exception {
        String token = "invalidToken";
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("123")
                .build();

        Mockito.doThrow(new JWTVerificationException("Invalid token")).when(tokenService).validateToken(token);

        mockMvc.perform(post("/room/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", token))
                        .content(new ObjectMapper().writeValueAsString(roomRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void joinRoom_shouldReturnBadRequestOnEmptyRoomIdRequest() throws Exception {
        RoomRequest nullRoomIdRequest = RoomRequest.builder().roomId(null).build();
        mockMvc.perform(post("/room/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", validToken))
                        .content(new ObjectMapper().writeValueAsString(nullRoomIdRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("RoomID should not be Null"));
    }

    @Test
    void joinRoom_shouldReturnBadRequestIfRoomDoesNotExist() throws Exception {
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("123")
                .build();
        Room room = Room.builder()
                .name("TestRoom")
                .uuid("123")
                .users(new ArrayList<>())
                .build();
        User user = User.builder()
                .name("TestUser")
                .password("test")
                .build();

        Mockito.when(tokenService.getUserNameFromToken(validToken)).thenReturn(user.getName());
        Mockito.doThrow(RoomNotFoundException.class).when(roomService).findRoomById(roomRequest.getRoomId());

        mockMvc.perform((post("/room/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", validToken))
                        .content(objectMapper.writeValueAsString(roomRequest))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Room doesn't exist."));
    }

    @Test
    void joinRoom_shouldReturnBadRequestIfUserDoesNotExist() throws Exception {
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("123")
                .build();
        Room room = Room.builder()
                .name("TestRoom")
                .uuid("123")
                .users(new ArrayList<>())
                .build();
        User user = User.builder()
                .name("TestUser")
                .password("test")
                .build();

        Mockito.when(tokenService.getUserNameFromToken(validToken)).thenReturn(user.getName());
        Mockito.when(roomService.findRoomById(room.getUuid())).thenReturn(room);
        Mockito.doThrow(UserNotFoundException.class).when(loginService).findByUsername(user.getName());

        mockMvc.perform((post("/room/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", validToken))
                        .content(objectMapper.writeValueAsString(roomRequest))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User doesn't exist."));
    }

    @Test
    void leaveRoom_shouldReturnOKIfInputIsValid() throws Exception {
        User user = User.builder()
                .name("Testuser")
                .password("test")
                .build();
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("123")
                .build();
        Room room = Room.builder()
                .uuid("123")
                .name("Testroom")
                .users(List.of(user))
                .build();

        Mockito.when(roomService.findRoomById(room.getUuid())).thenReturn(room);
        Mockito.when(tokenService.getUserNameFromToken(validToken)).thenReturn("Testuser");
        Mockito.when(loginService.findByUsername(user.getName())).thenReturn(user);

        mockMvc.perform((post("/room/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", validToken))
                        .content(objectMapper.writeValueAsString(roomRequest))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Testuser left the room"));

        Mockito.verify(userService, Mockito.times(1)).saveUser(user);
    }

    @Test
    void leaveRoom_shouldReturnUnauthorizedIfTokenIsInvalid() throws Exception {
        String invalidToken = "123";
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("123")
                .build();

        Mockito.doThrow(new JWTVerificationException("Invalid token"))
                .when(tokenService).validateToken(invalidToken);

        mockMvc.perform((post("/room/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", invalidToken))
                        .content(objectMapper.writeValueAsString(roomRequest))))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void leaveRoom_shouldReturnBadRequestIfRoomIdIsNull() throws Exception {
        RoomRequest roomRequest = RoomRequest.builder()
                .build();

        Mockito.doNothing().when(tokenService).validateToken(validToken);

        mockMvc.perform((post("/room/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", validToken))
                        .content(objectMapper.writeValueAsString(roomRequest))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void leaveRoom_shouldReturnBadRequestIfRoomDoesNotExist() throws Exception {
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("TestId")
                .build();

        Mockito.doNothing().when(tokenService).validateToken(validToken);
        Mockito.doThrow(RoomNotFoundException.class).when(roomService).findRoomById("TestId");

        mockMvc.perform((post("/room/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", validToken))
                        .content(objectMapper.writeValueAsString(roomRequest))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void leaveRoom_shouldReturnBadRequestIfUserNotFound() throws Exception {
        RoomRequest roomRequest = RoomRequest.builder()
                .roomId("123")
                .build();

        Mockito.when(roomService.findRoomById(null)).thenReturn(Room.builder().build());
        Mockito.doThrow(UserNotFoundException.class).when(loginService).findByUsername(Mockito.any());

        mockMvc.perform((post("/room/leave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", validToken))
                        .content(objectMapper.writeValueAsString(roomRequest))))
                .andExpect(status().isBadRequest());
    }
}