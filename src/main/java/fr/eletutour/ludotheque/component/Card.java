package fr.eletutour.ludotheque.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Card extends VerticalLayout {

    private Div header;
    private Div body;

    public Card(String title, Component... bodyComponents) {
        // Configuration du layout
        setWidthFull(); // La carte occupera toute la largeur par défaut
        addClassName("card");

        // Configuration de l'en-tête (header)
        header = new Div();
        header.setText(title);
        header.addClassName("card-title");


        // Configuration du corps (body)
        body = new Div();
        body.add(bodyComponents);
        body.addClassName("card-body");

        // Ajout du header et du body à la carte
        add(header, body);
    }

    // Méthode pour définir la taille de la carte
    public void setCardSize(String width, String height) {
        setWidth(width);
        setHeight(height);
    }

    // Méthode pour changer le contenu du corps
    public void setBodyContent(Component... components) {
        body.removeAll();
        body.add(components);
    }

    // Méthode pour changer le titre du header
    public void setTitle(String title) {
        header.setText(title);
    }
}
