package api.repositories.impl;

import api.models.entities.User;
import api.repositories.IRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends IRepository<User> {

    Mono<User> findByUsernameIgnoreCase(String username);

}
