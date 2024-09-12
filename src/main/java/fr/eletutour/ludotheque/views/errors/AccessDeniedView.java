package fr.eletutour.ludotheque.views.errors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import fr.eletutour.ludotheque.views.GamesListView;
import fr.eletutour.ludotheque.views.MainLayout;
import jakarta.servlet.http.HttpServletResponse;

@Route(value = "access-denied", layout = MainLayout.class)
@PageTitle("Accès refusé")
public class AccessDeniedView extends VerticalLayout implements HasErrorParameter<AccessDeniedException> {

    public AccessDeniedView() {
        add(new H1("Accès refusé"));
        add(new Paragraph("Vous n'avez pas la permission de voir cette page."));

        Button homeButton = new Button("Retour à l'accueil", event -> UI.getCurrent().navigate(GamesListView.class));
        add(homeButton);
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<AccessDeniedException> parameter) {
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
