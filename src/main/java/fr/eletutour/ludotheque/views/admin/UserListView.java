package fr.eletutour.ludotheque.views.admin;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.eletutour.ludotheque.configuration.security.Roles;
import fr.eletutour.ludotheque.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@Route(value="users", layout = MainLayout.class)
@PageTitle("Utilisateurs")
@RolesAllowed(Roles.ADMIN)
public class UserListView extends VerticalLayout {

}
