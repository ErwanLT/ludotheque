package fr.eletutour.ludotheque;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;


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
public class LudothequeApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "production");
		SpringApplication.run(LudothequeApplication.class, args);
	}

}
