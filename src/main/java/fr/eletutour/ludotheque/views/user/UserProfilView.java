package fr.eletutour.ludotheque.views.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.security.AuthenticationContext;
import fr.eletutour.ludotheque.dao.bean.AppUser;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.service.GameService;
import fr.eletutour.ludotheque.service.UserService;
import fr.eletutour.ludotheque.views.MainLayout;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Route(value = "profil", layout = MainLayout.class)
@PageTitle("Mon profil")
@PermitAll
public class UserProfilView extends VerticalLayout {

    private final UserService userService;
    private final GameService gameService;
    private final AuthenticationContext authenticationContext;
    private final PasswordEncoder passwordEncoder;
    private final Binder<AppUser> binder = new Binder<>(AppUser.class);

    private final TextField username = new TextField("Nom d'utilisateur");
    private final PasswordField password = new PasswordField("Nouveau mot de passe");
    private final TextArea bio = new TextArea("Biographie");
    private final Upload upload = new Upload();
    private final Image profileImage = new Image();
    private final EmailField email = new EmailField("Email");

    HorizontalLayout boutonLayout = new HorizontalLayout();
    Button exportButton = new Button("Exporter ma collection",VaadinIcon.DOWNLOAD.create(), e -> exportGames());
    Upload uploadButton = new Upload();

    private AppUser currentUser;
    private String passwordBeforeEdit;

    public UserProfilView(UserService userService, GameService gameService, AuthenticationContext authenticationContext, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.gameService = gameService;
        this.authenticationContext = authenticationContext;
        this.passwordEncoder = passwordEncoder;

        addClassName("profile-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Configuration des champs
        username.setReadOnly(true);
        username.setWidthFull(); // Prend toute la largeur
        password.setWidthFull(); // Prend toute la largeur
        bio.setWidthFull(); // Prend toute la largeur
        bio.setPlaceholder("Écrivez une courte biographie...");

        email.setWidthFull();
        email.setPlaceholder("Email de l'utilisateur");
        email.setClearButtonVisible(true);
        email.setPrefixComponent(VaadinIcon.ENVELOPE.create());

        uploadProfilImage();

        // Récupération et configuration des données utilisateur
        authenticationContext.getPrincipalName().ifPresent(name -> {
            currentUser = userService.findByUsername(name)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
            binder.setBean(currentUser);
            if (currentUser.getImage() != null) {
                showProfileImage(currentUser.getImage());
            } else {
                loadDefaultProfileImage();
            }
            passwordBeforeEdit = currentUser.getPassword();
        });

        binder.bindInstanceFields(this);
        password.clear();
        password.setPlaceholder("Laisser vide pour conserver le mot de passe actuel");

        // Bouton pour sauvegarder les modifications
        Button saveButton = new Button("Enregistrer", e -> saveProfile());
        saveButton.setWidthFull(); // Prend toute la largeur

        // Ajout d'un layout pour encapsuler le contenu et contrôler la largeur globale
        Div content = new Div(username, email, password, bio, upload, saveButton);
        content.setWidth("50%"); // 50% de la largeur de l'écran, ajustable selon besoin

        uploardCollection(gameService);
        boutonLayout.add(exportButton, uploadButton);

        add(
                new H1("Mon Profil"),
                profileImage,
                content,
                boutonLayout
        );
    }

    private void uploadProfilImage() {
        MemoryBuffer buffer = new MemoryBuffer();
        upload.setWidthFull(); // Prend toute la largeur
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFileSize(2 * 1024 * 1024); // 2 MB
        upload.setMaxFiles(1);
        upload.setReceiver(buffer);
        upload.addSucceededListener(event -> {
            try {
                currentUser.setImage(buffer.getInputStream().readAllBytes());
                Notification.show("Photo téléchargée avec succès");
            } catch (IOException e) {
                Notification error = Notification.show("Problème rencontré lors du téléchargement de la photo");
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

    private void uploardCollection(GameService gameService) {
        MemoryBuffer uploadBuffer = new MemoryBuffer();
        uploadButton.setAcceptedFileTypes("application/json");
        uploadButton.setReceiver(uploadBuffer);
        uploadButton.setMaxFiles(1);
        uploadButton.addSucceededListener(event -> {
            try {
                gameService.importGames(uploadBuffer.getInputStream());
                Notification success = Notification.show("Import réussi");
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
                Notification error = Notification.show("Erreur lors de l'importation des jeux");
                error.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

    private void showProfileImage(byte[] imageBytes) {
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        profileImage.setSrc("data:image/jpeg;base64," + base64Image);
        profileImage.setAlt("Image de profil");
        profileImage.setWidth("150px");
        profileImage.setHeight("150px");
        profileImage.getStyle().set("border-radius", "50%");
    }

    private void loadDefaultProfileImage() {
        profileImage.setSrc("images/default-profile.png");  // Chemin vers ton image par défaut
        profileImage.setAlt("Image par défaut");
        profileImage.setWidth("150px");
        profileImage.setHeight("150px");
        profileImage.getStyle().set("border-radius", "50%");
    }

    private void saveProfile() {
        try {
            binder.writeBean(currentUser);
            if (!password.isEmpty()) {
                currentUser.setPassword(passwordEncoder.encode(password.getValue()));
            } else {
                currentUser.setPassword(passwordBeforeEdit);
            }
            userService.saveUser(currentUser);
            Notification notification = Notification.show("Profil mis à jour avec succès !");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().getPage().reload();
        } catch (ValidationException e) {
            Notification notification = Notification.show("Erreur lors de la mise à jour du profil.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void exportGames() {
        try {
            // Récupérer la collection de jeux de l'utilisateur connecté
            List<JeuSociete> games = gameService.findAllGames("");

            // Convertir en JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(games);

            // Créer un fichier temporaire
            StreamResource resource = new StreamResource("collection.json", () -> {
                try {
                    return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                    Notification error = Notification.show("Erreur lors de l'exportation des jeux.");
                    error.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return null;
                }
            });

            final StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
            UI.getCurrent().getPage().open(registration.getResourceUri().toString());

        } catch (Exception e) {
            e.printStackTrace();
            Notification error = Notification.show("Erreur lors de l'exportation des jeux.");
            error.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
