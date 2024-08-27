package fr.eletutour.ludotheque.dao.repository;

import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Duration;
import java.util.List;

public interface JeuSocieteRepository extends JpaRepository<JeuSociete, Long> {

    List<JeuSociete> findByNomContainingIgnoreCase(String filtre);

    @Query("SELECT j FROM JeuSociete j WHERE " +
            "(:typeDeJeu IS NULL OR j.typeDeJeu = :typeDeJeu) AND " +
            "(:nombreDeJoueurs IS NULL OR :nombreDeJoueurs BETWEEN j.nombreJoueursMin AND j.nombreJoueursMax) AND " +
            "(:tempsDeJeu IS NULL OR j.tempsDeJeuEnMinutes <= :tempsDeJeu)")
    List<JeuSociete> findByCriteria(TypeJeu typeDeJeu, Integer nombreDeJoueurs, Duration tempsDeJeu);
}