package api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import api.service.UserDetailsService;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        //@formatter:off
        return httpSecurity
                .csrf().disable()
                .authorizeExchange()
                    .pathMatchers(HttpMethod.GET,
                            "/proficiencies/**",
                            "/items/**", "/ingredients/**").hasRole("USER")
                    .pathMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                    .pathMatchers(HttpMethod.POST).hasRole("ADMIN")
                    .pathMatchers(HttpMethod.PUT).hasRole("ADMIN")
                .pathMatchers("/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**").authenticated()
                .and()
                    .formLogin()
                .and()
                    .httpBasic()
                .and()
                .build();
        //@formatter:on
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

}
