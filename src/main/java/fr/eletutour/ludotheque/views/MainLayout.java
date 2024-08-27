package fr.eletutour.ludotheque.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;


public class MainLayout extends AppLayout {
    Button toggleThemeButton;

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Ma ludothèque");

        // Spacer pour pousser les composants suivants à droite
        HorizontalLayout spacer = new HorizontalLayout();
        spacer.setWidthFull();

        // Bouton pour basculer entre les thèmes
        toggleThemeButton = new Button(VaadinIcon.ADJUST.create());
        toggleThemeButton.setText("Thème sombre");
        toggleThemeButton.addClickListener(click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                toggleThemeButton.setText("Thème sombre");
            } else {
                themeList.add(Lumo.DARK);
                toggleThemeButton.setText("Thème clair");
            }
        });

        // Header layout avec logo, spacer et bouton de bascule du thème
        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                spacer,          // Ajouter le spacer ici pour pousser le bouton à droite
                toggleThemeButton
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        // Ajouter le header à la barre de navigation
        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Liste de jeux", GamesListView.class);
        RouterLink dashboardLink = new RouterLink("Dashboard", DashboardView.class);
        RouterLink randomGameLink = new RouterLink("Jeu Aléatoire", RandomGameSearchView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink,
                dashboardLink,
                randomGameLink
        ));
    }
}
