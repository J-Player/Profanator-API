package api.configs.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

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
                        "/webjars/**").authenticated()
                .and()
                    .formLogin().disable()
                    .httpBasic()
                .and()
                .build();
        //@formatter:on
    }

    private ServerAuthenticationSuccessHandler authenticationSuccessHandler() {
        final String location = "swagger-ui.html";
        return (webFilterExchange, authentication) -> {
            ServerWebExchange exchange = webFilterExchange.getExchange();
            ServerRequestCache requestCache = new WebSessionServerRequestCache();
            ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();
            return requestCache.getRedirectUri(exchange).defaultIfEmpty(URI.create(location))
                    .flatMap(uri -> redirectStrategy.sendRedirect(exchange, uri));
        };
    }

    @Bean
    @Profile("!prod")
    protected MapReactiveUserDetailsService userDetailsService() {
        PasswordEncoder passwordEncoder = passwordEncoder();
        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();
        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode("user"))
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(admin, user);
    }

    @Bean
    protected ReactiveAuthenticationManager authenticationManager(@Autowired ReactiveUserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}


