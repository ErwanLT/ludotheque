package fr.eletutour.ludotheque.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;
import fr.eletutour.ludotheque.service.GameService;
import fr.eletutour.ludotheque.views.form.GameForm;

import java.io.ByteArrayInputStream;

@Route("/jeux")
@PageTitle("Mes jeux")
public class GamesListView extends VerticalLayout{

    private final GameService service;
    private final Grid<JeuSociete> grid = new Grid<>(JeuSociete.class);
    private final TextField filterText = new TextField();
    private GameForm gameForm;


    public GamesListView(GameService service) {
        this.service = service;
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
    }

    private void updateList() {
        grid.setItems(service.findAllGames(filterText.getValue()));
        grid.getColumns().getFirst()
                .setFooter(String.format("%s jeux", service.countGames()));
    }

    private void configureForm() {
        gameForm = new GameForm();
        gameForm.setWidth("25em");
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, gameForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, gameForm);
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("filtre par nom...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Ajouter jeu");

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {

        grid.setSizeFull();
        grid.setColumns("id");
        grid.addColumn(JeuSociete::getNom).setHeader("Nom").setSortable(true);
        grid.addColumn(JeuSociete::getTypeDeJeu).setHeader("Type").setSortable(true);
        grid.addColumn(jeu -> "de " + jeu.getNombreJoueursMin() + " à " + jeu.getNombreJoueursMax() +" joueurs").setHeader("Nombre de joueurs");
        grid.addColumn(JeuSociete::getFormattedTempsDeJeu).setHeader("Temps de jeu");
        grid.addColumn(jeu -> "à partir de :" + jeu.getAgeMinimum() + "ans").setHeader("Age minimum");
        grid.addComponentColumn(jeu -> {
            Image img = createImageFromBytes(jeu.getImage(), jeu.getNom());
            img.setHeight(150, Unit.PIXELS);
            img.setWidth(150, Unit.PIXELS);
            return img;
        });

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    public static Image createImageFromBytes(byte[] imageBytes, String altText) {
        StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(imageBytes));
        return new Image(resource, altText);
    }
}
