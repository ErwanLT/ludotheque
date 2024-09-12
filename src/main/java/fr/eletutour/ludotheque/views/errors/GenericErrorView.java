package fr.eletutour.ludotheque.views.errors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.eletutour.ludotheque.views.GamesListView;
import fr.eletutour.ludotheque.views.MainLayout;

@Route(value = "error", layout = MainLayout.class)
@PageTitle("Erreur")
public class GenericErrorView extends VerticalLayout {

    public GenericErrorView() {
        add(new H1("Erreur interne"));
        add(new Paragraph("Une erreur s'est produite. Veuillez réessayer plus tard."));
        
        Button homeButton = new Button("Retour à l'accueil", event -> UI.getCurrent().navigate(GamesListView.class));
        add(homeButton);
        setAlignItems(Alignment.CENTER);
    }
}
