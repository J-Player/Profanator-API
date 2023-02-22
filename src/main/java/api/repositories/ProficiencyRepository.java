package api.repositories;

import api.domains.Proficiency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProficiencyRepository extends ReactiveCrudRepository<Proficiency, UUID>,
        ReactiveSortingRepository<Proficiency, UUID> {

    Mono<Proficiency> findByNameIgnoreCase(String name);

}
