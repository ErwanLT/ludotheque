package fr.eletutour.ludotheque.dao.repository;

import fr.eletutour.ludotheque.dao.bean.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findByUsernameContainingIgnoreCase(String filtre);
    long countByRole(String role);
}
