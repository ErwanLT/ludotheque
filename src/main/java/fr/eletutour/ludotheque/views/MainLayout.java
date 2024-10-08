package fr.eletutour.ludotheque.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.Lumo;
import fr.eletutour.ludotheque.views.admin.UserListView;
import fr.eletutour.ludotheque.views.dashboards.DashboardCombinedView;
import fr.eletutour.ludotheque.views.dashboards.DashboardTypeView;


public class MainLayout extends AppLayout {
    Button toggleThemeButton;
    private final AuthenticationContext authenticationContext;

    public MainLayout(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Ma ludothèque");

        // Spacer pour pousser les composants suivants à droite
        HorizontalLayout spacer = new HorizontalLayout();
        spacer.setWidthFull();

        MenuBar menuBar = new MenuBar();
        MenuItem item = menuBar.addItem(authenticationContext.getPrincipalName().orElse(""));
        SubMenu subMenu = item.getSubMenu();
        subMenu.addItem("Profil", menuItemClickEvent -> UI.getCurrent().navigate("profil"));
        subMenu.add(new Hr());
        subMenu.addItem("Logout", menuItemClickEvent -> authenticationContext.logout());

        // Bouton pour basculer entre les thèmes
        toggleThemeButton = new Button(VaadinIcon.ADJUST.create());
        toggleThemeButton.addClickListener(click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });

        // Header layout avec logo, spacer et bouton de bascule du thème
        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                spacer,
                toggleThemeButton,
                menuBar
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        SideNav sideNav = new SideNav();

        SideNavItem listNavItem = new SideNavItem("Liste de jeux", GamesListView.class, VaadinIcon.LIST.create());

        SideNavItem dashboardNavItem = new SideNavItem("Dashboard");
        dashboardNavItem.setPrefixComponent(VaadinIcon.DASHBOARD.create());
        SideNavItem overviewNavItem = new SideNavItem("Jeux par type", DashboardTypeView.class, VaadinIcon.PIE_CHART.create());
        dashboardNavItem.addItem(overviewNavItem);
        SideNavItem combinedNavItem = new SideNavItem("Type / extension", DashboardCombinedView.class, VaadinIcon.LINE_CHART.create());
        dashboardNavItem.addItem(combinedNavItem);

        sideNav.addItem(listNavItem);
        sideNav.addItem(dashboardNavItem);  // Dashboard avec ses sous-items
        sideNav.addItem(new SideNavItem("Jeu Aléatoire", RandomGameSearchView.class, VaadinIcon.RANDOM.create()));

        if(authenticationContext.hasRole("ADMIN")){
            SideNavItem adminNav = new SideNavItem("Administrateur");
            adminNav.setPrefixComponent(VaadinIcon.USER_CARD.create());
            SideNavItem users = new SideNavItem("Utilisateurs", UserListView.class, VaadinIcon.USERS.create());
            adminNav.addItem(users);

            sideNav.addItem(adminNav);
        }
        // Ajoute le SideNav au Drawer
        addToDrawer(sideNav);
    }
}
