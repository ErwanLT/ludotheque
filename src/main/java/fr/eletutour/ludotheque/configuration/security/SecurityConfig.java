package fr.eletutour.ludotheque.configuration.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import fr.eletutour.ludotheque.dao.repository.UserRepository;
import fr.eletutour.ludotheque.views.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@EnableWebSecurity 
@EnableMethodSecurity(jsr250Enabled = true) 
@Configuration
class SecurityConfig extends VaadinWebSecurity {

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http); 
        setLoginView(http, LoginView.class);
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.accessDeniedHandler(accessDeniedHandler)
        );
    }

    @Bean
    public UserDetailsService users(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(), // Le mot de passe est déjà encodé
                        AuthorityUtils.createAuthorityList(user.getRole())))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}