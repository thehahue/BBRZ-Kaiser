package at.bbrz.kaiser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KaiserApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaiserApplication.class, args);

	}

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
