package api.services;

import api.domains.User;
import api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService, AbstractService<User> {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .cast(UserDetails.class);
    }

    @Override
    public Mono<User> findById(UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException(null))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o item (id = {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                });
    }

    public Mono<User> findByName(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(monoResponseStatusNotFoundException(username));
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> save(User user) {
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user)
                .doOnNext(u -> u.setPassword(rawPassword));
    }

    @Override
    public Mono<Void> update(User user) {
        return findById(user.getId())
                .doOnNext(userRepository::save)
                .then();
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return findById(id)
                .flatMap(userRepository::delete);
    }

    private <T> Mono<T> monoResponseStatusNotFoundException(String username) {
        String message = username != null && username.length() > 0 ? String.format("User '%s' not found", username) : "User not found";
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

}
