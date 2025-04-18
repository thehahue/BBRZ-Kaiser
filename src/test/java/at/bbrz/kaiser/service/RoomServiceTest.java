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
}