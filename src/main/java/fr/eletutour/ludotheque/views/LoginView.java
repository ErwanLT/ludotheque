package fr.eletutour.ludotheque.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("login") 
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private static final Logger log = LoggerFactory.getLogger(LoginView.class);
	private final LoginForm login = new LoginForm();

	public LoginView() {
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		// Personnaliser les messages en français
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.getForm().setTitle("Connexion");
		i18n.getForm().setUsername("Nom d'utilisateur");
		i18n.getForm().setPassword("Mot de passe");
		i18n.getForm().setSubmit("Se connecter");
		i18n.getErrorMessage().setTitle("Erreur d'authentification");
		i18n.getErrorMessage().setMessage("Nom d'utilisateur ou mot de passe incorrect");
		i18n.getErrorMessage().setUsername("Nom d'utilisateur obligatoire");
		i18n.getErrorMessage().setPassword("Mot de passe obligatoire");
		i18n.setAdditionalInformation("Veuillez entrer vos identifiants");

		login.setI18n(i18n);
		login.setAction("login");
		login.setForgotPasswordButtonVisible(false);

		// Ajouter un bouton pour créer un compte
		Button createAccountButton = new Button("Créer un compte", event -> {
			UI.getCurrent().navigate("register");  // Naviguer vers une page d'enregistrement
		});

		add(new H1("Ludothèque"), login, createAccountButton);
	}


	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if(beforeEnterEvent.getLocation()  
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            login.setError(true);
        }
	}
}