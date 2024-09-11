package fr.eletutour.ludotheque.views.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.eletutour.ludotheque.configuration.security.Roles;
import fr.eletutour.ludotheque.dao.bean.AppUser;
import fr.eletutour.ludotheque.service.UserService;
import fr.eletutour.ludotheque.views.MainLayout;
import fr.eletutour.ludotheque.views.form.UserForm;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route(value="users", layout = MainLayout.class)
@PageTitle("Utilisateurs")
@RolesAllowed(Roles.ADMIN)
public class UserListView extends VerticalLayout {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private Grid<AppUser> userGrid = new Grid<>(AppUser.class);
    private final TextField filterText = new TextField();
    private UserForm userForm = new UserForm();

    public UserListView(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void configureForm() {
        userForm = new UserForm();
        userForm.setWidth("25em");
        userForm.addListener(UserForm.SaveEvent.class, this::saveUser);
        userForm.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        userForm.addListener(UserForm.CloseEvent.class, e -> closeEditor());
        userForm.setPasswordEncoder(passwordEncoder);
    }

    private void deleteUser(UserForm.DeleteEvent deleteEvent) {

    }

    private void saveUser(UserForm.SaveEvent saveEvent) {
        userService.saveUser(saveEvent.getUser());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        userGrid.setSizeFull();
        userGrid.setColumns("id", "username", "role");
        userGrid.addColumn("password").setVisible(false);
        userGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        userGrid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue(), true));
    }

    private void editUser(AppUser user, boolean editing) {
        if (user == null) {
            closeEditor();
        } else {
            userForm.setUser(user, user.getPassword(),editing);
            userForm.setVisible(true);
        }
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(userGrid, userForm);
        content.setFlexGrow(2, userGrid);
        content.setFlexGrow(1, userForm);
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("filtre par nom...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Ajouter utilisateur");
        addContactButton.addClickListener(click -> addUser());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        userGrid.asSingleSelect().clear();
        editUser(new AppUser(), false);
    }

    private void updateList() {
        userGrid.setItems(userService.findAllUsers(filterText.getValue()));
        userGrid.getColumns().getFirst().setFooter(String.format("%s jeux", userService.countUsers()));
    }

    private void closeEditor() {
        userForm.setUser(null, null, true);
        userForm.setVisible(false);
        removeClassName("editing");
    }

}
