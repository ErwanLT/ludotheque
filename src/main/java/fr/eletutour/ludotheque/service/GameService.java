package fr.eletutour.ludotheque.service;

import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;
import fr.eletutour.ludotheque.dao.repository.JeuSocieteRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class GameService {

    private final JeuSocieteRepository repository;

    public GameService(JeuSocieteRepository repository) {
        this.repository = repository;
    }

    public List<JeuSociete> findAllGames(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return repository.findAll();
        } else {
            return repository.findByNomContainingIgnoreCase(stringFilter);
        }
    }

    public long countGames() {
        return repository.count();
    }

    public void deleteGame(JeuSociete jeuSociete) {
        repository.delete(jeuSociete);
    }

    public void saveGame(JeuSociete jeuSociete) {
        if (jeuSociete == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        repository.save(jeuSociete);
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
