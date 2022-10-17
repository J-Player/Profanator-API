package api.configs.security;

import api.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Slf4j
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        final String ADMIN = "ADMIN";
        //@formatter:off
        return httpSecurity
                .csrf().disable()
                .authorizeExchange()
                    .pathMatchers(HttpMethod.GET,
                            "/proficiencies/**",
                            "/items/**", "/ingredients/**").authenticated()
                    .pathMatchers("/users/**").hasRole(ADMIN)
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
    protected ReactiveAuthenticationManager authenticationManager(UserService userService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
