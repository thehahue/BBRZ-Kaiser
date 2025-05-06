package at.bbrz.kaiser.service;

import at.bbrz.kaiser.exceptions.RoomNotFoundException;
import at.bbrz.kaiser.model.Room;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    RoomService roomService;

    @Mock
    RoomRepository roomRepository;

    @Mock
    Room room;

    @Test
    void findNameByIdWithValidIdReturnsName() {
        String validId = "valid";


        Mockito.when(roomRepository.findById(validId)).thenReturn(Optional.of(room));
        Mockito.when(room.getName()).thenReturn("room");

        assertEquals("room", roomService.findNameById(validId));
    }

    @Test
    void findNameByIdWithInvalidIdThrowsRoomNotFoundException() {
        String invalidId = "invalid";

        Mockito.when(roomRepository.findById(invalidId)).thenThrow(new RoomNotFoundException("Room not found!"));

        RoomNotFoundException roomNotFoundException = assertThrows(RoomNotFoundException.class, () -> roomService.findUsersById(invalidId));

        assertEquals("Room not found!", roomNotFoundException.getMessage());
    }

    @Test
    void findUsersByIdWithValidIdReturnsUsers() {
        String validId = "valid";
        List<User> users = new ArrayList<>();


        Mockito.when(roomRepository.findById(validId)).thenReturn(Optional.of(room));
        Mockito.when(room.getUsers()).thenReturn(users);

        List<User> usersById = roomService.findUsersById(validId);
        assertEquals(users, usersById);
    }

    @Test
    void findRoomByIDReturnsEmptyOptional() {
        Mockito.when(roomRepository.findById("invalidID")).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.findNameById("invalidID"));

    }

    @Test
    void findUsersByIDReturnsEmptyOptional() {
        Mockito.when(roomRepository.findById("invalidID")).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.findUsersById("invalidID"));
    }

    @Test
    void findRoomById_returnsRoom_whenRoomExists() {
        String id = "123";

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.ofNullable(Room.builder()
                .uuid(id)
                .build()));

        Room result = roomService.findRoomById(id);

        assertNotNull(result);
        assertEquals(id, result.getUuid());
    }

    @Test
    void findRoomById_shouldThrowException_whenRoomNotFound() {
        String id = "unknown";

        Mockito.when(roomRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.findRoomById(id));
    }


    @Test
    void saveRoomSavesRoom() {
        Room room1 = new Room();
        roomService.saveRoom(room);
        Mockito.verify(roomRepository, Mockito.times(1)).save(room);
    }
}
