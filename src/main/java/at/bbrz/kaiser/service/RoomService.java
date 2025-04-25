package at.bbrz.kaiser.service;

import at.bbrz.kaiser.exceptions.RoomNotFoundException;
import at.bbrz.kaiser.model.Room;
import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    public String findNameById(String id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);

        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException("Room doesn't exist");
        }

        return optionalRoom.get().getName();
    }

    public List<User> findUsersById(String id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);

        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException("Room doesn't exist");
        }

        return optionalRoom.get().getUsers();
    }

    public Room findRoomById(String id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);

        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException("Room doesn't exist");
        }

        return optionalRoom.get();
    }

    public void saveRoom(Room room) {
        roomRepository.save(room);
    }
}
