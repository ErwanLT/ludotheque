package fr.eletutour.ludotheque;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Theme("my-theme")
public class LudothequeApplication implements AppShellConfigurator {

	@Autowired
	private JeuSocieteRepository jeuSocieteRepository;

	public static void main(String[] args) {
		SpringApplication.run(LudothequeApplication.class, args);
	}

}
