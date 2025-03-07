package at.bbrz.kaiser.repository;

import at.bbrz.kaiser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByNameAndPassword(String username, String password);
}
