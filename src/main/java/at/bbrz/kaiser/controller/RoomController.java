package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.exceptions.RoomNotFoundException;
import at.bbrz.kaiser.exceptions.UserNotFoundException;
import at.bbrz.kaiser.model.Room;
import at.bbrz.kaiser.model.RoomRequest;
import at.bbrz.kaiser.model.RoomResponse;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.service.LoginService;
import at.bbrz.kaiser.service.RoomService;
import at.bbrz.kaiser.service.TokenService;
import at.bbrz.kaiser.service.UUIDWrapper;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class RoomController {

    private final UUIDWrapper uuidWrapper;
    private final RoomService roomService;
    private final TokenService tokenService;
    private final LoginService loginService;

    @Autowired
    public RoomController(RoomService roomService, TokenService tokenService, UUIDWrapper uuidWrapper, LoginService loginService) {
        this.uuidWrapper = uuidWrapper;
        this.roomService = roomService;
        this.tokenService = tokenService;
        this.loginService = loginService;
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomResponse> getRoomDetails(@PathVariable String roomId, @CookieValue(value = "token") String token) {
        if (!isTokenValid(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RoomResponse.builder().build());

        try {
            return ResponseEntity.ok(RoomResponse.builder()
                    .uuid(roomId)
                    .name(roomService.findNameById(roomId))
                    .users(roomService.findUsersById(roomId))
                    .build());

        } catch (RoomNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RoomResponse.builder()
                    .uuid(roomId)
                    .build());
        }

    }

    @PostMapping("/room/createRoom")
    public ResponseEntity<RoomResponse> createNewRoom(@RequestBody RoomRequest roomRequest, @CookieValue(value = "token") String token) {
        if (!isTokenValid(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RoomResponse.builder().build());
        if (roomRequest.getName() == null) {
            return ResponseEntity.badRequest().body(RoomResponse.builder().message("RoomName should not be Null").build());
        }

        Room newRoom = Room.builder()
                .name(roomRequest.getName())
                .users(new ArrayList<>())
                .uuid(uuidWrapper.createUUID())
                .build();

        roomService.saveRoom(newRoom);

        return ResponseEntity.ok(RoomResponse.builder()
                .uuid(newRoom.getUuid())
                .name(newRoom.getName())
                .build());
    }

    @PostMapping("/room/join")
    public ResponseEntity<RoomResponse> joinRoom(@RequestBody RoomRequest roomRequest, @CookieValue(value = "token") String token) {
        if (!isTokenValid(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RoomResponse.builder().build());
        if (roomRequest.getRoomId() == null) {
            return ResponseEntity.badRequest().body(RoomResponse.builder().message("RoomID should not be Null").build());
        }
        Room room;
        User user;
        try {
            room = roomService.findRoomById(roomRequest.getRoomId());
            user = loginService.findByUsername(tokenService.getUserNameFromToken(token));
        } catch (RoomNotFoundException exception) {
            return ResponseEntity.badRequest().body(RoomResponse.builder()
                    .message("Room doesn't exist.")
                    .build());
        } catch (UserNotFoundException exception) {
            return ResponseEntity.badRequest().body(RoomResponse.builder()
                    .message("User doesn't exist.")
                    .build());
        }

        user.setRoom(room);
        room.getUsers().add(user);

        roomService.saveRoom(room);

        return ResponseEntity.ok(RoomResponse.builder()
                .name(room.getName())
                .users(room.getUsers())
                .build());
    }

    private boolean isTokenValid(String token) {
        try {
            tokenService.validateToken(token);
        } catch (JWTVerificationException e) {
            return false;
        }
        return true;
    }
}
