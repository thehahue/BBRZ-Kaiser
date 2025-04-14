package at.bbrz.kaiser.repository;

import at.bbrz.kaiser.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}