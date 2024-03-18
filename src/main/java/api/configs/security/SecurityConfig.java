package api.configs.security;

import api.models.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    @Bean
<<<<<<< HEAD
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity, SecurityFilter securityFilter) {
        final String ADMIN = UserRole.ADMIN.name();
        final String[] whitelist = {
                "/proficiencies/**",
                "/items/**",
                "/ingredients/**",
=======
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        final String ADMIN = "ADMIN";
        //@formatter:off
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(c -> c
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
                        "/webjars/**").authenticated())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
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
>>>>>>> main
        };
        final String[] swagger = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers(HttpMethod.GET, whitelist).permitAll()
                                .pathMatchers(HttpMethod.POST, whitelist).hasRole(ADMIN)
                                .pathMatchers(HttpMethod.PUT, whitelist).hasRole(ADMIN)
                                .pathMatchers(HttpMethod.DELETE, whitelist).hasRole(ADMIN)
                                .pathMatchers(HttpMethod.POST, "/users").hasRole(ADMIN)
                                .pathMatchers(HttpMethod.GET, "/trade/**").permitAll()
                                .pathMatchers("/auth/**").permitAll()
                                .pathMatchers("/actuator/**").hasRole(ADMIN)
                                .pathMatchers(swagger).permitAll()
                                .anyExchange().authenticated()
                )
                .addFilterAt(securityFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }

    @Bean
    protected ReactiveAuthenticationManager authenticationManager(@Autowired ReactiveUserDetailsService userDetailsService) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        //TODO: Configurar adequadamente o CORS
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedOriginPatterns(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}