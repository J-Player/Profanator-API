package api.services.impl;

import api.domains.User;
import api.domains.dtos.UserDTO;
import api.repositories.UserRepository;
import api.services.IService;
import api.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@Profile("prod")
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService, IService<User, UserDTO> {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .cast(UserDetails.class);
    }

    @Override
    public Mono<User> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException())
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o item (id = {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                });
    }

    public Mono<User> findByName(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    @Override
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> save(UserDTO userDTO) {
        return userRepository.save(MapperUtil.MAPPER.map(userDTO, User.class));
    }

    @Override
    public Mono<Void> update(UserDTO userDTO, Long id) {
        return findById(id)
                .doOnNext(user -> MapperUtil.MAPPER.map(userDTO, user))
                .flatMap(userRepository::save)
                .then();
    }

    @Override
    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(userRepository::delete);
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

}
