package by.quantumquartet.quanthink.config;

import by.quantumquartet.quanthink.security.JwtAuthEntryPoint;
import by.quantumquartet.quanthink.security.JwtAuthTokenFilter;
import by.quantumquartet.quanthink.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthEntryPoint unauthorizedHandler;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfiguration(JwtAuthEntryPoint unauthorizedHandler, UserDetailsServiceImpl userDetailsService) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final HttpMethod GET = HttpMethod.GET;
        final HttpMethod POST = HttpMethod.POST;
        final HttpMethod PUT = HttpMethod.PUT;
        final HttpMethod DELETE = HttpMethod.DELETE;

        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(GET, "/users").hasRole("ADMIN")
                        .requestMatchers(GET, "/users/{id}").hasRole("ADMIN")
                        .requestMatchers(PUT, "/users/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(PUT, "/users/{id}/assignAdminRole").hasRole("ADMIN")
                        .requestMatchers(DELETE, "/users/{id}").hasRole("ADMIN")

                        .requestMatchers(GET, "calculations").hasRole("ADMIN")
                        .requestMatchers(GET, "/calculations/{id}").hasRole("ADMIN")
                        .requestMatchers(GET, "/calculations/user/{userId}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(DELETE, "/calculations/{id}").hasRole("ADMIN")
                        .requestMatchers(DELETE, "/calculations/user/{userId}").hasAnyRole("ADMIN", "USER")

                        .anyRequest().permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
