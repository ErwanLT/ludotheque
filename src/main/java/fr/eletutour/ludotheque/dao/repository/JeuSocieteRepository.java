package fr.eletutour.ludotheque.dao.repository;

import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JeuSocieteRepository extends JpaRepository<JeuSociete, Long> {

    List<JeuSociete> findByNomContainingIgnoreCase(String filtre);
}