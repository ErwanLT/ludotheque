package fr.eletutour.ludotheque.service;

import fr.eletutour.ludotheque.dao.bean.AppUser;
import fr.eletutour.ludotheque.dao.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> findAllUsers(String stringFilter){
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.findByUsernameContainingIgnoreCase(stringFilter);
        }
    }

    public Optional<AppUser> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public void saveUser(AppUser user) {
        userRepository.save(user);
    }

    public void deleteUser(AppUser user) {
        if (user.getRole().equals("ROLE_ADMIN")) {
            // Compte le nombre d'utilisateurs avec le rôle "ROLE_ADMIN"
            long adminCount = userRepository.countByRole("ROLE_ADMIN");

            // Si c'est le seul administrateur, on empêche la suppression
            if (adminCount <= 1) {
                throw new IllegalStateException("Impossible de supprimer cet administrateur car c'est le seul présent.");
            }
        }
        userRepository.delete(user);
    }

    public void register(String username, String encodedPassword) {
        AppUser appUser = new AppUser(username, encodedPassword, "ROLE_USER");
        userRepository.save(appUser);
    }
}
