package api.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import api.domain.User;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    Mono<User> findByUsername(String username);

}
