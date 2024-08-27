package fr.eletutour.ludotheque.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Route(value = "random-game", layout = MainLayout.class)
@PageTitle("Jeu Aléatoire | Ma Ludothèque")
public class RandomGameSearchView extends VerticalLayout {

    private final JeuSocieteRepository jeuSocieteRepository;

    private ComboBox<TypeJeu> typeJeu = new ComboBox<>("Type de jeu");
    private NumberField nombreDeJoueurs = new NumberField("Nombre de joueurs");
    private NumberField tempsDeJeu = new NumberField("Temps de jeu maximum (minutes)");
    private Button searchButton = new Button("Chercher un jeu aléatoire");
    private Div result = new Div();

    public RandomGameSearchView(JeuSocieteRepository jeuSocieteRepository) {
        this.jeuSocieteRepository = jeuSocieteRepository;

        typeJeu.setItems(TypeJeu.values());
        typeJeu.setItemLabelGenerator(TypeJeu::name);

        nombreDeJoueurs.setMin(1);
        tempsDeJeu.setMin(1);

        searchButton.addClickListener(e -> {
            Optional<JeuSociete> jeuAleatoire = findRandomGame(
                typeJeu.getValue(),
                nombreDeJoueurs.getValue() != null ? nombreDeJoueurs.getValue().intValue() : null,
                tempsDeJeu.getValue() != null ? Duration.ofMinutes(tempsDeJeu.getValue().longValue()) : null
            );
            displayResult(jeuAleatoire);
        });

        add(typeJeu, nombreDeJoueurs, tempsDeJeu, searchButton, result);
    }

    private Optional<JeuSociete> findRandomGame(TypeJeu typeJeu, Integer nombreDeJoueurs, Duration tempsDeJeu) {
        List<JeuSociete> jeux = jeuSocieteRepository.findByCriteria(typeJeu, nombreDeJoueurs, tempsDeJeu);
        if (jeux.isEmpty()) {
            return Optional.empty();
        }

        Random rand = new Random();
        return Optional.of(jeux.get(rand.nextInt(jeux.size())));
    }

    private void displayResult(Optional<JeuSociete> jeuAleatoire) {
        if (jeuAleatoire.isPresent()) {
            JeuSociete jeu = jeuAleatoire.get();
            result.setText("Jeu sélectionné : " + jeu.getNom() +
                " (Type : " + jeu.getTypeDeJeu() +
                ", Joueurs : " + jeu.getNombreJoueursMin() + "-" + jeu.getNombreJoueursMax() +
                ", Durée : " + jeu.getTempsDeJeuEnMinutes().toMinutes() + " minutes)");
        } else {
            result.setText("Aucun jeu ne correspond à vos critères.");
        }
    }
}
