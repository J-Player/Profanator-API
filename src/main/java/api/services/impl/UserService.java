package api.services.impl;

<<<<<<< HEAD
import api.exceptions.ResourceNotFoundException;
import api.models.entities.User;
import api.repositories.impl.UserRepository;
=======
import api.domains.User;
import api.domains.dtos.UserDTO;
import api.repositories.UserRepository;
>>>>>>> main
import api.services.IService;
import api.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService, IService<User, UserDTO> {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .cast(UserDetails.class);
    }

    @Override
<<<<<<< HEAD
    public Mono<User> findById(Integer id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")))
                .doOnError(ex -> log.error("Ocorreu um erro ao recuperar o item (id = {}): {}", id, ex.getMessage()));
    }

    public Mono<User> findByName(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")));
=======
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
>>>>>>> main
    }

    @Override
    public Mono<Page<User>> findAll(Pageable pageable) {
        return userRepository.findAllBy(pageable)
                .collectList()
                .zipWith(userRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    @Override
    public Mono<User> save(UserDTO userDTO) {
        return userRepository.save(MapperUtil.MAPPER.map(userDTO, User.class));
    }

    @Override
<<<<<<< HEAD
    public Mono<Void> update(User user) {
        return findById(user.getId())
                .doOnNext(savedUser -> user.setCreatedAt(savedUser.getCreatedAt()))
                .thenReturn(user)
=======
    public Mono<Void> update(UserDTO userDTO, Long id) {
        return findById(id)
                .doOnNext(user -> MapperUtil.MAPPER.map(userDTO, user))
>>>>>>> main
                .flatMap(userRepository::save)
                .then();
    }

    @Override
<<<<<<< HEAD
    public Mono<Void> delete(Integer id) {
=======
    public Mono<Void> delete(Long id) {
>>>>>>> main
        return findById(id)
                .flatMap(userRepository::delete);
    }

<<<<<<< HEAD
    public boolean verifyUser(UserDetails userDetails) {
        boolean accountNonLocked = userDetails.isAccountNonLocked();
        boolean accountNonExpired = userDetails.isAccountNonExpired();
        boolean credentialsNonExpired = userDetails.isCredentialsNonExpired();
        boolean enabled = userDetails.isEnabled();
        return accountNonExpired && accountNonLocked && credentialsNonExpired && enabled;
    }

    public Mono<Void> deleteAll() {
        return userRepository.deleteAll();
=======
    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
>>>>>>> main
    }

}
