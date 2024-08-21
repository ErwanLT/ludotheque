package fr.eletutour.ludotheque;

import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
	public void run(String... args) {
		// Initialisation des jeux de société
		JeuSociete monopoly = new JeuSociete(
				"Monopoly",
				2,
				8,
				8,
				TypeJeu.PLATEAU,
				Duration.ofMinutes(120),
				null
		);

		JeuSociete catan = new JeuSociete(
				"Les Colons de Catane",
				3,
				4,
				10,
				TypeJeu.STRATEGIE,
				Duration.ofMinutes(90),
				null
		);

		JeuSociete uno = new JeuSociete(
				"Uno",
				2,
				10,
				7,
				TypeJeu.CARTES,
				Duration.ofMinutes(30),
				null
		);

		// Sauvegarder les jeux dans la base de données
		jeuSocieteRepository.saveAll(Arrays.asList(monopoly, catan, uno));
	}
}
