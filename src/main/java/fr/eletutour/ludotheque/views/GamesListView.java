package fr.eletutour.ludotheque.views;


import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.service.GameService;
import fr.eletutour.ludotheque.views.form.GameForm;

import java.io.ByteArrayInputStream;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Mes jeux")
public class GamesListView extends VerticalLayout {

    private final GameService service;
    private final TreeGrid<JeuSociete> treeGrid = new TreeGrid<>(JeuSociete.class);
    private final TextField filterText = new TextField();
    private GameForm gameForm;

    public GamesListView(GameService service) {
        this.service = service;
        setSizeFull();
        configureTreeGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void updateList() {
        treeGrid.setItems(service.findAllGames(filterText.getValue()), JeuSociete::getExtensions);
        treeGrid.getColumns().getFirst().setFooter(String.format("%s jeux", service.countGames()));
    }

    private void configureForm() {
        gameForm = new GameForm();
        gameForm.setWidth("25em");
        gameForm.addListener(GameForm.SaveEvent.class, this::saveGame);
        gameForm.addListener(GameForm.DeleteEvent.class, this::deleteGame);
        gameForm.addListener(GameForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteGame(GameForm.DeleteEvent deleteEvent) {
        service.deleteGame(deleteEvent.getJeuSociete());
        updateList();
        closeEditor();
    }

    private void saveGame(GameForm.SaveEvent saveEvent) {
        service.saveGame(saveEvent.getJeuSociete());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(treeGrid, gameForm);
        content.setFlexGrow(2, treeGrid);
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
        addContactButton.addClickListener(click -> addGame());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureTreeGrid() {
        treeGrid.setSizeFull();
        treeGrid.removeAllColumns();
        treeGrid.setItems(service.findAllGames(filterText.getValue()), JeuSociete::getExtensions);

        treeGrid.addHierarchyColumn(JeuSociete::getNom).setHeader("Nom").setSortable(true);
        treeGrid.addComponentColumn(jeu -> {
            UnorderedList listType = new UnorderedList();
            jeu.getTypeDeJeu().forEach(t -> listType.add(new ListItem(t.name())));
            return listType;
        }).setHeader("Type");
        treeGrid.addColumn(jeu -> "de " + jeu.getNombreJoueursMin() + " à " + jeu.getNombreJoueursMax() + " joueurs").setHeader("Nombre de joueurs");
        treeGrid.addColumn(JeuSociete::getFormattedTempsDeJeu).setHeader("Temps de jeu");
        treeGrid.addColumn(jeu -> "à partir de :" + jeu.getAgeMinimum() + " ans").setHeader("Age minimum");
        treeGrid.addComponentColumn(jeu -> {
            Image img = createImageFromBytes(jeu.getImage(), jeu.getNom());
            img.setHeight(150, Unit.PIXELS);
            img.setWidth(150, Unit.PIXELS);
            return img;
        });

        treeGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        treeGrid.asSingleSelect().addValueChangeListener(event -> editGame(event.getValue()));
    }

    public static Image createImageFromBytes(byte[] imageBytes, String altText) {
        StreamResource resource = new StreamResource("image", () -> new ByteArrayInputStream(imageBytes));
        return new Image(resource, altText);
    }

    private void closeEditor() {
        gameForm.setJeuSociete(null);
        gameForm.setVisible(false);
        removeClassName("editing");
    }

    public void editGame(JeuSociete jeuSociete) {
        if (jeuSociete == null) {
            closeEditor();
        } else {
            gameForm.setJeuSociete(jeuSociete);
            gameForm.setVisible(true);
        }
    }

    private void addGame() {
        treeGrid.asSingleSelect().clear();
        editGame(new JeuSociete());
    }
}
