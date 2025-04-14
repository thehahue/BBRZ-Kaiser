package at.bbrz.kaiser.controller;

import at.bbrz.kaiser.model.RoomResponse;
import at.bbrz.kaiser.service.RoomService;
import at.bbrz.kaiser.service.TokenService;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    private final RoomService roomService;
    private final TokenService tokenService;

    @Autowired
    public RoomController(RoomService roomService, TokenService tokenService) {
        this.roomService = roomService;
        this.tokenService =tokenService;
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomResponse> getRoomDetails(@PathVariable String roomId, @CookieValue(value = "token") String token) {
        try {
            tokenService.validateToken(token);
        } catch (JWTVerificationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RoomResponse.builder().success(false).build());
        }


        return ResponseEntity.ok(RoomResponse.builder()
                .uuid(roomId)
                .name(roomService.findNameById(roomId))
                .users(roomService.findUsersById(roomId))
                .success(true)
                .build());
    }
}
