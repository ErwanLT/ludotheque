package fr.eletutour.ludotheque;

import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;

@SpringBootApplication
public class LudothequeApplication implements CommandLineRunner {

	@Autowired
	private JeuSocieteRepository jeuSocieteRepository;

	public static void main(String[] args) {
		SpringApplication.run(LudothequeApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {

		byte[] unoImage = loadImage("images/uno.png");
		byte[] monopolyImage = loadImage("images/monopoly.png");
		byte[] catanImage = loadImage("images/catan.png");

		// Initialisation des jeux de société
		JeuSociete monopoly = new JeuSociete(
				"Monopoly",
				TypeJeu.PLATEAU,
				2,
				8,
				8,
				Duration.ofMinutes(120),
				monopolyImage
		);

		JeuSociete catan = new JeuSociete(
				"Les Colons de Catane",
				TypeJeu.STRATEGIE,
				3,
				4,
				10,
				Duration.ofMinutes(90),
				catanImage
		);

		JeuSociete uno = new JeuSociete(
				"Uno",
				TypeJeu.CARTES,
				2,
				10,
				7,
				Duration.ofMinutes(30),
				unoImage
		);

		// Sauvegarder les jeux dans la base de données
		jeuSocieteRepository.saveAll(Arrays.asList(monopoly, catan, uno));
	}

	private byte[] loadImage(String path) throws IOException {
		ClassPathResource imgFile = new ClassPathResource(path);
		return Files.readAllBytes(imgFile.getFile().toPath());
	}
}
