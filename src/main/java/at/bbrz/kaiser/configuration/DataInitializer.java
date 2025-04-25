package at.bbrz.kaiser.configuration;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
@Profile({"development","default"})
public class DataInitializer {
    @Autowired
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initDatabaseData() {
        userRepository.saveAll(List.of(
                User.builder()
                        .name("admin")
                        .password("password")
                        .build(),
                User.builder()
                        .name("user")
                        .password("password")
                        .build()
        ));
    }
}
