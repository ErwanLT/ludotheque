package fr.eletutour.ludotheque.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import fr.eletutour.ludotheque.dao.bean.AppUser;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;
import fr.eletutour.ludotheque.dao.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private JeuSocieteRepository jeuSocieteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationContext authenticationContext;

    @InjectMocks
    private GameService gameService;

    private AppUser currentUser;

    @BeforeEach
    public void setUp() {
        // Initialisation d'un utilisateur pour les tests
        currentUser = new AppUser("user", "password", "ROLE_USER");
    }

    @Test
    public void testFindAllGames_NoFilter_ReturnsAllGamesForUser() {
        // Simulation du nom d'utilisateur et de l'utilisateur courant
        when(authenticationContext.getPrincipalName()).thenReturn(Optional.of("user"));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(currentUser));

        // Simulation des jeux pour cet utilisateur
        List<JeuSociete> expectedGames = Collections.singletonList(new JeuSociete());
        when(jeuSocieteRepository.findAllByOwner(currentUser)).thenReturn(expectedGames);

        // Appel du service
        List<JeuSociete> actualGames = gameService.findAllGames(null);

        // Vérification
        assertEquals(expectedGames, actualGames);
        verify(jeuSocieteRepository, times(1)).findAllByOwner(currentUser);
    }

    @Test
    public void testFindAllGames_WithFilter_ReturnsFilteredGames() {
        // Simulation du nom d'utilisateur et de l'utilisateur courant
        when(authenticationContext.getPrincipalName()).thenReturn(Optional.of("user"));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(currentUser));

        // Simulation des jeux filtrés
        List<JeuSociete> expectedGames = Collections.singletonList(new JeuSociete());
        when(jeuSocieteRepository.findByNomContainingIgnoreCaseAndOwner("filter", currentUser))
                .thenReturn(expectedGames);

        // Appel du service avec un filtre
        List<JeuSociete> actualGames = gameService.findAllGames("filter");

        // Vérification
        assertEquals(expectedGames, actualGames);
        verify(jeuSocieteRepository, times(1))
                .findByNomContainingIgnoreCaseAndOwner("filter", currentUser);
    }

    @Test
    public void testSaveGame() {
        // Simulation du nom d'utilisateur et de l'utilisateur courant
        when(authenticationContext.getPrincipalName()).thenReturn(Optional.of("user"));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(currentUser));

        JeuSociete jeuSociete = new JeuSociete();

        // Appel de la méthode save
        gameService.saveGame(jeuSociete);

        // Vérification
        assertEquals(currentUser, jeuSociete.getOwner());
        verify(jeuSocieteRepository, times(1)).save(jeuSociete);
    }

    @Test
    public void testDeleteGame_WhenIsExtension() {
        // Simulation d'un jeu qui est une extension
        JeuSociete jeuPrincipal = new JeuSociete();
        JeuSociete extension = new JeuSociete();
        extension.setJeuPrincipal(jeuPrincipal);
        extension.setEstExtension(true);

        // Appel de la méthode delete
        gameService.deleteGame(extension);

        // Vérification que l'extension a été supprimée de la liste des extensions du jeu principal
        assertFalse(jeuPrincipal.getExtensions().contains(extension));
        verify(jeuSocieteRepository, times(1)).save(jeuPrincipal);
        verify(jeuSocieteRepository, times(1)).delete(extension);
    }

    @Test
    public void testFindAllGames_NoUserFound_ReturnsEmptyList() {
        // Simulation du nom d'utilisateur et de l'absence d'utilisateur dans la base de données
        when(authenticationContext.getPrincipalName()).thenReturn(Optional.of("user"));
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        // Appel du service
        List<JeuSociete> actualGames = gameService.findAllGames(null);

        // Vérification que la liste retournée est vide
        assertTrue(actualGames.isEmpty());
        verify(jeuSocieteRepository, never()).findAllByOwner(any());
    }

    @Test
    public void testCountGames() {
        // Simulation du comptage de jeux
        when(jeuSocieteRepository.count()).thenReturn(5L);

        // Appel de la méthode countGames
        long gameCount = gameService.countGames();

        // Vérification du résultat
        assertEquals(5L, gameCount);
        verify(jeuSocieteRepository, times(1)).count();
    }

    @Test
    public void testFindRandomGame_WithFilters() {
        // Simulation des jeux disponibles pour l'utilisateur
        when(authenticationContext.getPrincipalName()).thenReturn(Optional.of("user"));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(currentUser));

        // Création des jeux avec des setters (ou un autre constructeur si nécessaire)
        JeuSociete jeu1 = new JeuSociete();
        jeu1.setNom("Jeu1");
        jeu1.setNombreJoueursMin(2);
        jeu1.setNombreJoueursMax(4);
        jeu1.setTempsDeJeuEnMinutes(Duration.ofMinutes(30));

        JeuSociete jeu2 = new JeuSociete();
        jeu2.setNom("Jeu2");
        jeu2.setNombreJoueursMin(3);
        jeu2.setNombreJoueursMax(6);
        jeu2.setTempsDeJeuEnMinutes(Duration.ofMinutes(60));

        List<JeuSociete> jeux = List.of(jeu1, jeu2);
        when(jeuSocieteRepository.findAllByOwner(currentUser)).thenReturn(jeux);

        // Appel de la méthode findRandomGame avec des filtres
        List<JeuSociete> filteredGames = gameService.findRandomGame(null, 4, Duration.ofMinutes(60));

        // Vérification du résultat : les jeux doivent correspondre aux critères
        assertEquals(2, filteredGames.size());
        verify(jeuSocieteRepository, times(1)).findAllByOwner(currentUser);
    }


    @Test
    public void testFindRandomGame_NoMatchingFilters() {
        // Simulation des jeux disponibles pour l'utilisateur
        when(authenticationContext.getPrincipalName()).thenReturn(Optional.of("user"));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(currentUser));

        // Création des jeux avec des setters (ou un autre constructeur)
        JeuSociete jeu1 = new JeuSociete();
        jeu1.setNom("Jeu1");
        jeu1.setNombreJoueursMin(2);
        jeu1.setNombreJoueursMax(4);
        jeu1.setTempsDeJeuEnMinutes(Duration.ofMinutes(30));

        JeuSociete jeu2 = new JeuSociete();
        jeu2.setNom("Jeu2");
        jeu2.setNombreJoueursMin(3);
        jeu2.setNombreJoueursMax(6);
        jeu2.setTempsDeJeuEnMinutes(Duration.ofMinutes(60));

        List<JeuSociete> jeux = List.of(jeu1, jeu2);
        when(jeuSocieteRepository.findAllByOwner(currentUser)).thenReturn(jeux);

        // Appel de la méthode findRandomGame avec des filtres ne correspondant à aucun jeu
        List<JeuSociete> filteredGames = gameService.findRandomGame(null, 10, Duration.ofMinutes(120));

        // Vérification : aucun jeu ne correspond
        assertTrue(filteredGames.isEmpty());
        verify(jeuSocieteRepository, times(1)).findAllByOwner(currentUser);
    }


    @Test
    public void testDeleteGame_WhenNotAnExtension() {
        // Simulation d'un jeu qui n'est pas une extension
        JeuSociete jeu = new JeuSociete();
        jeu.setEstExtension(false);

        // Appel de la méthode delete
        gameService.deleteGame(jeu);

        // Vérification que le jeu est bien supprimé
        verify(jeuSocieteRepository, times(1)).delete(jeu);
        verify(jeuSocieteRepository, never()).save(any(JeuSociete.class));
    }
}
