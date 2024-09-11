package fr.eletutour.ludotheque.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import fr.eletutour.ludotheque.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route("register")
@PageTitle("Créer un compte")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
    private final TextField username = new TextField("Nom d'utilisateur");
    private final PasswordField password = new PasswordField("Mot de passe");
    private final Button registerButton = new Button("S'enregistrer");
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterView(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        addClassName("register-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        registerButton.addClickListener(event -> {
            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Tous les champs doivent être remplis", 3000, Notification.Position.MIDDLE);
            } else {
                String encodedPassword = passwordEncoder.encode(password.getValue());
                userService.register(username.getValue(), encodedPassword);
                Notification.show("Compte créé avec succès", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().navigate("login");
            }
        });

        add(username, password, registerButton);
    }
}
