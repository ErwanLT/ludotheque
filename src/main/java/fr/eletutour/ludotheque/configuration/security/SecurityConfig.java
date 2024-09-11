package fr.eletutour.ludotheque.configuration.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import fr.eletutour.ludotheque.dao.repository.UserRepository;
import fr.eletutour.ludotheque.views.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity 
@EnableMethodSecurity(jsr250Enabled = true) 
@Configuration
class SecurityConfig extends VaadinWebSecurity { 

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http); 
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService users(UserRepository userRepository) {

        /*var bob = User.builder()
                .username("bob")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles(Roles.USER)
                .build();
        var admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
                .roles(Roles.ADMIN, Roles.USER)
                .build();
        return new InMemoryUserDetailsManager(alice, bob, admin);
         */

        return username -> userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(), // Le mot de passe est déjà encodé
                        AuthorityUtils.createAuthorityList(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}