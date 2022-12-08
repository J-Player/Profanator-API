package api.configs.data;

import api.domains.User;
import api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.saveAll(List.of(
                        User.builder()
                                .username("admin")
                                .authorities("ROLE_ADMIN,ROLE_USER")
                                .password("admin")
                                .build(),
                        User.builder()
                                .username("user")
                                .authorities("ROLE_USER")
                                .password("user")
                                .build()))
                .thenMany(userRepository.findAll().doOnSubscribe(s -> log.info("Buscando usuários...")))
                .subscribe(user -> log.info("User: {}", user),
                        ex -> log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage()),
                        () -> log.info("Busca finalizada."));
    }

}