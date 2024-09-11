package fr.eletutour.ludotheque;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import fr.eletutour.ludotheque.dao.bean.AppUser;
import fr.eletutour.ludotheque.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@Theme("my-theme")
@PWA(
		name = "Ludotheque",
		shortName = "LudoApp",
		iconPath = "images/icon.png",
		description = "Application de gestion de ludothèque",
		offlinePath = "offline.html",
		offlineResources = {"images/icon.png"}
)
public class LudothequeApplication implements CommandLineRunner, AppShellConfigurator {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "production");
		SpringApplication.run(LudothequeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (userRepository.findByUsername("admin").isEmpty()) {
			// Crée un nouvel utilisateur admin
			AppUser admin = new AppUser();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("password")); // Assure-toi que le mot de passe est sécurisé
			admin.setRole("ROLE_ADMIN");

			userRepository.save(admin);
		}
	}
}
