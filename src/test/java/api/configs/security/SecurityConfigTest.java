package api.configs.security;

import api.integration.constraint.IntegrationConstraints;
import api.models.entities.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import java.util.List;

@Profile("test")
@TestConfiguration
public class SecurityConfigTest {

    @Bean
    @Primary
    public ReactiveUserDetailsService userDetailsService() {
        return new MapReactiveUserDetailsService(List.of(
                User.builder()
                        .username(IntegrationConstraints.ADMIN_USER)
                        .password(IntegrationConstraints.ADMIN_PASSWORD)
                        .role(IntegrationConstraints.ADMIN_ROLE)
                        .build(),
                User.builder()
                        .username(IntegrationConstraints.REGULAR_USER)
                        .password(IntegrationConstraints.REGULAR_PASSWORD)
                        .role(IntegrationConstraints.USER_ROLE)
                        .build()));
    }

}
