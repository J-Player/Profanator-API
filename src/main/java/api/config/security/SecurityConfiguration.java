package api.config.security;

import api.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final String ADMIN = "ADMIN";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        //@formatter:off
        return httpSecurity
                .csrf().disable()
                .authorizeExchange()
                    .pathMatchers(HttpMethod.GET,
                            "/proficiencies/**",
                            "/items/**", "/ingredients/**").permitAll()
                    .pathMatchers(HttpMethod.POST).hasRole(ADMIN)
                    .pathMatchers(HttpMethod.PUT).hasRole(ADMIN)
                    .pathMatchers(HttpMethod.DELETE).hasRole(ADMIN)
                .pathMatchers("/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**").permitAll()
                .and()
                    .formLogin()
                .and()
                    .httpBasic()
                .and()
                .build();
        //@formatter:on
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager(UserService userService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userService);
    }

}
