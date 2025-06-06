package at.bbrz.kaiser.repository;

import at.bbrz.kaiser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    User findByNameAndPassword(String username, String password);

    Optional<User> findByName(String username);
}
