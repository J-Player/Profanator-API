package api.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import api.domain.Proficiency;
import reactor.core.publisher.Mono;

public interface ProficiencyRepository extends ReactiveCrudRepository<Proficiency, Integer> {

    Mono<Proficiency> findByNameIgnoreCase(String name);

}
