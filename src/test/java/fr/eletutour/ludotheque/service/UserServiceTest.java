package fr.eletutour.ludotheque.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.eletutour.ludotheque.dao.bean.AppUser;
import fr.eletutour.ludotheque.dao.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AppUser adminUser;
    private AppUser normalUser;

    @BeforeEach
    public void setUp() {
        adminUser = new AppUser("admin", "encodedPassword", "ROLE_ADMIN");
        normalUser = new AppUser("user", "encodedPassword", "ROLE_USER");
    }

    @Test
    public void testFindAllUsers_NoFilter_ReturnsAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(adminUser, normalUser));

        List<AppUser> users = userService.findAllUsers(null);

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllUsers_WithFilter_ReturnsFilteredUsers() {
        when(userRepository.findByUsernameContainingIgnoreCase("admin"))
                .thenReturn(Arrays.asList(adminUser));

        List<AppUser> users = userService.findAllUsers("admin");

        assertEquals(1, users.size());
        assertEquals("admin", users.get(0).getUsername());
        verify(userRepository, times(1)).findByUsernameContainingIgnoreCase("admin");
    }

    @Test
    public void testCountUsers() {
        when(userRepository.count()).thenReturn(2L);

        long count = userService.countUsers();

        assertEquals(2, count);
        verify(userRepository, times(1)).count();
    }

    @Test
    public void testSaveUser() {
        userService.saveUser(adminUser);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteUser_AdminWithMultipleAdmins_SuccessfulDeletion() {
        when(userRepository.countByRole("ROLE_ADMIN")).thenReturn(2L);

        userService.deleteUser(adminUser);

        verify(userRepository, times(1)).delete(adminUser);
    }

    @Test
    public void testDeleteUser_SoleAdmin_ThrowsException() {
        when(userRepository.countByRole("ROLE_ADMIN")).thenReturn(1L);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userService.deleteUser(adminUser);
        });

        assertEquals("Impossible de supprimer cet administrateur car c'est le seul présent.", exception.getMessage());
        verify(userRepository, times(0)).delete(adminUser);
    }

    @Test
    public void testDeleteUser_NormalUser_SuccessfulDeletion() {
        userService.deleteUser(normalUser);

        verify(userRepository, times(1)).delete(normalUser);
    }

    @Test
    public void testRegister_SavesNewUser() {
        String username = "newUser";
        String encodedPassword = "encodedPassword";

        userService.register(username, encodedPassword);

        AppUser expectedUser = new AppUser(username, encodedPassword, "ROLE_USER");
        verify(userRepository, times(1)).save(any());
    }
}
