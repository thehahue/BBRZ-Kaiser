package at.bbrz.kaiser.configuration;

import at.bbrz.kaiser.filter.JWTFilter;
import at.bbrz.kaiser.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    private TokenService tokenService;

    @Autowired
    public SecurityConfiguration(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF deaktivieren, weil wir JWT verwenden
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/*.html").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .anyRequest().authenticated() // Alles andere braucht Authentifizierung
                )
                .addFilterBefore(new JWTFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
