package fr.eletutour.ludotheque.component;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import fr.eletutour.components.rating.StarRating;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;

import java.io.ByteArrayInputStream;


public class GameDetails extends VerticalLayout {

    private final Div gameName = new Div();
    private final Div rating = new Div();
    private final Div image = new Div();
    private final Div featuresTitle = new Div();

    public GameDetails(JeuSociete jeu) {
        configureName(jeu);
        configureRating(jeu);
        configureImage(jeu);
        configureFeatures(jeu);
        configureExtension(jeu);
    }

    private void configureExtension(JeuSociete jeu) {
        if (!jeu.getExtensions().isEmpty()) {
            Div extensionTitle = new Div();
            extensionTitle.setText("Extensions");
            extensionTitle.setSizeFull();
            extensionTitle.addClassName("featureTitle");
            add(extensionTitle);

            HorizontalLayout extensionsLayout = new HorizontalLayout();
            extensionsLayout.setSpacing(true);
            extensionsLayout.addClassName("extensionsLayout");

            jeu.getExtensions().forEach(extension -> {
                Card card = createExtensionCard(extension);  // Utiliser le composant Card
                extensionsLayout.add(card);
            });

            add(extensionsLayout);
        }
    }

    private Card createExtensionCard(JeuSociete extension) {
        // Création du corps de la carte avec l'image
        StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(extension.getImage()));
        Image extensionImage = new Image(resource, extension.getNom());
        extensionImage.setWidth("100%");  // L'image prend toute la largeur de la carte

        Div extensionRating = new Div();
        StarRating starRating = new StarRating(10, "20px", extension.getRating());
        extensionRating.add(starRating.getStarLayout());
        extensionRating.addClassName("rating");

        // Création du composant Card avec le titre de l'extension et l'image
        Card extensionCard = new Card(extension.getNom(), extensionImage, extensionRating);

        // Définir la taille de la carte
        extensionCard.setCardSize("300px", "auto");  // Par exemple, 300px de largeur, hauteur automatique

        return extensionCard;
    }

    private void configureFeatures(JeuSociete jeu) {
        featuresTitle.setText("Caractéristiques");
        featuresTitle.setSizeFull();
        featuresTitle.addClassName("featureTitle");
        add(featuresTitle);

        Span time = new Span();
        time.setText("⏱ Temps de jeu : " + jeu.getFormattedTempsDeJeu());
        time.addClassName("features");
        add(time);

        Span joeurs = new Span();
        joeurs.setText("🎲 Nombre de joueurs : de " + jeu.getNombreJoueursMin() + " à " + jeu.getNombreJoueursMax() + "joueurs");
        joeurs.addClassName("features");
        add(joeurs);

        Span age = new Span();
        age.setText("🧩 Age minimum : à partir de " + jeu.getAgeMinimum() + " ans");
        age.addClassName("features");
        add(age);
    }

    private void configureImage(JeuSociete jeu) {
        StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(jeu.getImage()));
        Image gameImage = new Image(resource, jeu.getNom());
        gameImage.setWidth("25%");

        image.add(gameImage);
        image.addClassName("gameImage");
        add(image);
    }

    private void configureRating(JeuSociete jeu) {
        StarRating starRating = new StarRating(10);
        starRating.setRating(jeu.getRating());

        rating.add(starRating.getStarLayout());
        rating.addClassName("rating");
        add(rating);
    }

    private void configureName(JeuSociete jeu) {
        gameName.setText(jeu.getNom());
        gameName.setSizeFull();
        gameName.addClassName("gameName");
        add(gameName);
    }
}
