package at.bbrz.kaiser;

import at.bbrz.kaiser.model.User;
import at.bbrz.kaiser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import java.util.List;

@SpringBootApplication
public class KaiserApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(KaiserApplication.class, args);

	}
	@Profile("development")
	@EventListener(ApplicationReadyEvent.class)
	private void initDatabaseData(){
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
