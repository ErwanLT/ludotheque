package fr.eletutour.ludotheque.component;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@Tag("div")
public class StarRating extends AbstractField<StarRating, Double> {

    private final HorizontalLayout starLayout = new HorizontalLayout();
    private final Icon[] stars = new Icon[10];

    public StarRating() {
        super(0.0); // Valeur initiale par défaut

        // Configuration du layout des étoiles
        starLayout.setSpacing(false); // Réduit l'espacement entre les étoiles

        // Ajoute les étoiles au layout
        for (int i = 0; i < stars.length; i++) {
            stars[i] = VaadinIcon.STAR.create();
            stars[i].setSize("32px");
            stars[i].setColor("gray");
            int index = i;
            stars[i].addClickListener(e -> setRating(index + 1.0));
            starLayout.add(stars[i]); // Ajoute l'icône au layout starLayout
        }

    }

    public HorizontalLayout getStarLayout() {
        return starLayout;
    }

    public void setRating(double rating) {
        setModelValue(rating, true);
        updateStars();
    }

    @Override
    protected void setPresentationValue(Double rating) {
        updateStars();
    }

    private void updateStars() {
        for (int i = 0; i < stars.length; i++) {
            if (i < getValue()) {
                stars[i].setColor("gold");
            } else {
                stars[i].setColor("gray");
            }
        }
    }
}
