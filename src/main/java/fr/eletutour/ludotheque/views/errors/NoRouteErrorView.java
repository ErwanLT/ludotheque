package fr.eletutour.ludotheque.views.errors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import fr.eletutour.ludotheque.views.GamesListView;
import fr.eletutour.ludotheque.views.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;

@Route(value = "not-found", layout = MainLayout.class)
@PageTitle("Page non trouvée")
@PermitAll
public class NoRouteErrorView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    public NoRouteErrorView() {
        add(new H1("Page non trouvé"));
        add(new Paragraph("La page que vous cherchez n'existe pas."));

        Button homeButton = new Button("Retour à l'accueil", event -> UI.getCurrent().navigate(GamesListView.class));
        add(homeButton);
        setAlignItems(Alignment.CENTER);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<NotFoundException> errorParameter) {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
