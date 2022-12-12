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
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.deleteAll().thenReturn(
                userRepository.saveAll(List.of(
                        User.builder()
                                .username("admin")
                                .password("admin")
                                .authorities("ROLE_ADMIN,ROLE_USER")
                                .build(),
                        User.builder()
                                .username("user")
                                .password("user")
                                .authorities("ROLE_USER")
                                .build())));
    }

}