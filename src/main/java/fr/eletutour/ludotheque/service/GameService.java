package fr.eletutour.ludotheque.service;

import com.vaadin.flow.spring.security.AuthenticationContext;
import fr.eletutour.ludotheque.dao.bean.AppUser;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;
import fr.eletutour.ludotheque.dao.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final JeuSocieteRepository jeuSocieteRepository;
    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public GameService(JeuSocieteRepository jeuSocieteRepository, UserRepository userRepository, AuthenticationContext authenticationContext) {
        this.jeuSocieteRepository = jeuSocieteRepository;
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    public List<JeuSociete> findAllGames(String stringFilter) {
        String currentUsername = authenticationContext.getPrincipalName().get(); // Récupère le nom d'utilisateur

        Optional<AppUser> currentUser = userRepository.findByUsername(currentUsername); // Cherche l'utilisateur dans la base

        if (currentUser.isPresent()) {
            if (stringFilter == null || stringFilter.isEmpty()) {
                return jeuSocieteRepository.findAllByOwner(currentUser.get()).stream()
                        .filter(j -> !j.isEstExtension())
                        .toList();
            } else {
                return jeuSocieteRepository.findByNomContainingIgnoreCaseAndOwner(stringFilter, currentUser.get()).stream()
                        .filter(j -> !j.isEstExtension())
                        .toList();
            }
        }

        // Gérer le cas où l'utilisateur n'est pas trouvé
        return Collections.emptyList();
    }

    public long countGames() {
        return jeuSocieteRepository.count();
    }

    public void deleteGame(JeuSociete jeuSociete) {
        if (jeuSociete.isEstExtension() && jeuSociete.getJeuPrincipal() != null) {
            JeuSociete jeuPrincipal = jeuSociete.getJeuPrincipal();
            jeuPrincipal.getExtensions().remove(jeuSociete);
            jeuSocieteRepository.save(jeuPrincipal);
        }
        jeuSocieteRepository.delete(jeuSociete);
    }

    public void saveGame(JeuSociete jeuSociete) {
        if (jeuSociete == null) {
            System.err.println("Game is null. Are you sure you have connected your form to the application?");
            return;
        }
        jeuSociete.getExtensions()
                .forEach(
                    ex -> {
                        if(ex.getRating() == null) {
                            ex.setRating(ex.getJeuPrincipal().getRating());
                        }
                    }
        );
        String username = authenticationContext.getPrincipalName().get();
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        jeuSociete.setOwner(user);
        jeuSocieteRepository.save(jeuSociete);
    }

    public List<JeuSociete> findRandomGame(TypeJeu typeJeu, Integer nombreDeJoueurs, Duration tempsDeJeu) {
        List<JeuSociete> jeux = findAllGames("");

        return jeux.stream()
                .filter(jeu -> {
                    if(typeJeu == null){
                        return true;
                    }
                    return jeu.getTypeDeJeu().contains(typeJeu);
                })
                .filter(jeu -> {
                    if (nombreDeJoueurs == null){
                        return true;
                    }
                    return jeu.getNombreJoueursMin() <= nombreDeJoueurs && jeu.getNombreJoueursMax() >= nombreDeJoueurs;
                })
                .filter(jeu -> {
                    if(tempsDeJeu == null){
                        return true;
                    }
                    return jeu.getTempsDeJeuEnMinutes().toMinutes() <= tempsDeJeu.toMinutes();
                })
                .toList();

    }
}
