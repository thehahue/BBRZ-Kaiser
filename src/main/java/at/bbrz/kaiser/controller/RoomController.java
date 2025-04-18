package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.exceptions.RoomNotFoundException;
import at.bbrz.kaiser.model.Room;
import at.bbrz.kaiser.model.RoomResponse;
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

    @Autowired
    public RoomController(RoomService roomService, TokenService tokenService, UUIDWrapper uuidWrapper) {
        this.uuidWrapper = uuidWrapper;
        this.roomService = roomService;
        this.tokenService = tokenService;
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomResponse> getRoomDetails(@PathVariable String roomId, @CookieValue(value = "token") String token) {
        try {
            tokenService.validateToken(token);
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RoomResponse.builder().build());
        }

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
    public ResponseEntity<RoomResponse> createNewRoom(@RequestBody String roomName, @CookieValue(value = "token") String token) {
        try {
            tokenService.validateToken(token);
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RoomResponse.builder().build());
        }

        Room newRoom = Room.builder()
                .name(roomName)
                .users(new ArrayList<>())
                .uuid(uuidWrapper.createUUID())
                .build();

        roomService.saveRoom(newRoom);

        return ResponseEntity.ok(RoomResponse.builder()
                .uuid(newRoom.getUuid())
                .name(newRoom.getName())
                .build());
    }
}
