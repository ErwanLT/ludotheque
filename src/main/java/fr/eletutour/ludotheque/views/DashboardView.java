package fr.eletutour.ludotheque.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard")
@JsModule("https://cdn.jsdelivr.net/npm/chart.js")
public class DashboardView extends VerticalLayout {

    private final JeuSocieteRepository repository;

    public DashboardView(JeuSocieteRepository repository) {
        this.repository = repository;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();
        Div chartContainer = new Div();
        chartContainer.getElement().setProperty("innerHTML", "<canvas id='chartCanvas' width='1400' height='1200'></canvas>");
        chartContainer.setSizeFull();
        add(chartContainer);

        loadChart();
    }

    private void loadChart() {
        List<JeuSociete> jeux = repository.findAll();
        Map<TypeJeu, Long> jeuxParType = jeux.stream()
                .collect(Collectors.groupingBy(JeuSociete::getTypeDeJeu, Collectors.counting()));

        // Préparer les données pour Chart.js
        String labels = jeuxParType.keySet().stream()
                .map(Enum::name)
                .collect(Collectors.joining("','", "'", "'"));
        String data = jeuxParType.values().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String js = """
                console.log("Création du graphique en cours !");
                const ctx = document.getElementById('chartCanvas').getContext('2d');
                new Chart(ctx, {
                    type: 'pie',
                    data: {
                        labels:[%s],
                        datasets: [{
                            data: [%s],
                             backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40', '#8C564B', '#E377C2', '#7F7F7F', '#BCBD22']
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false
                    }
                });
                """.formatted(labels, data);
        UI.getCurrent().getPage().executeJs(js);
    }
}
