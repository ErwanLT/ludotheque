package fr.eletutour.ludotheque.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;

@Route("/jeux")
@PageTitle("Mes jeux")
public class GamesListView extends VerticalLayout{

    private final JeuSocieteRepository repository;
    private Grid<JeuSociete> grid = new Grid<>(JeuSociete.class);


    public GamesListView(JeuSocieteRepository repository) {
        this.repository = repository;
        setSizeFull();

        configureGrid();
        add(grid);
    }

    private void configureGrid() {

        var jeux = repository.findAll();

        grid.setSizeFull();
        grid.setColumns("id");
        grid.addColumn(JeuSociete::getNom).setHeader("Nom");
        grid.addColumn(JeuSociete::getNombreJoueurs).setHeader("Nombre de joueurs");
        grid.addColumn(JeuSociete::getTempsDeJeu).setHeader("Temps de jeu");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setItems(jeux);
    }
}
