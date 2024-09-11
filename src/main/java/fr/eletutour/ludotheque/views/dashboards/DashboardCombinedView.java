package fr.eletutour.ludotheque.views.dashboards;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;
import fr.eletutour.ludotheque.service.GameService;
import fr.eletutour.ludotheque.views.MainLayout;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "combined", layout = MainLayout.class)
@PageTitle("Jeux par type et extensions")
@JsModule("https://cdn.jsdelivr.net/npm/chart.js")
@PermitAll
public class DashboardCombinedView extends DashboardBaseView {

    private final GameService gameService;

    public DashboardCombinedView(GameService gameService) {
        this.gameService = gameService;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();
        Div chartContainer = new Div();
        chartContainer.getElement().setProperty("innerHTML", "<canvas id='chartCanvas' width='1400' height='1200'></canvas>");
        chartContainer.setSizeFull();
        add(chartContainer);

        loadChart();
    }

    private void loadChart() {
        List<JeuSociete> jeux = gameService.findAllGames("");

        // Dataset 1: Jeux par type
        Map<TypeJeu, Long> jeuxParType = jeux.stream()
                .flatMap(jeu -> jeu.getTypeDeJeu().stream()) // Aplatir les types de jeux en une seule liste
                .collect(Collectors.groupingBy(typeJeu -> typeJeu, Collectors.counting())); // Regrouper par type et compter

        // Dataset 2: Extensions par type
        Map<TypeJeu, Long> extensionsParType = jeux.stream()
                .filter(JeuSociete::isEstExtension) // Filtrer les extensions
                .flatMap(jeu -> jeu.getTypeDeJeu().stream()) // Aplatir les types de jeux en une seule liste
                .collect(Collectors.groupingBy(typeJeu -> typeJeu, Collectors.counting())); // Regrouper par type et compter

        String labels = jeuxParType.keySet().stream()
                .map(Enum::name)
                .collect(Collectors.joining("','", "'", "'"));
        String dataJeux = jeuxParType.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String dataExtensions = jeuxParType.keySet().stream()
                .map(type -> extensionsParType.getOrDefault(type, 0L))
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String js = """
                console.log("Création du graphique combiné en cours !");
                const ctx = document.getElementById('chartCanvas').getContext('2d');
                new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels:[%s],
                        datasets: [
                            {
                               label: 'Jeux par type',
                               data: [%s],
                               borderColor: "#d40f39",
                               backgroundColor: "#d40f5e",
                             },
                            {
                               label: 'Nombre extension',
                               data: [%s],
                               borderColor: "#0f22d4",
                               backgroundColor: "#0f7bd4"
                            }
                        ]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                position: 'top',
                            },
                            title: {
                                display: true,
                                text: 'Nombre de jeux par type et nombre d\\'extensions'
                            }
                        }
                    }
                });
                """.formatted(labels, dataJeux, dataExtensions);

        UI.getCurrent().getPage().executeJs(js);
    }
}
