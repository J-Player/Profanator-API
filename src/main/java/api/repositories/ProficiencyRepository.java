package api.repositories;

import api.domains.Proficiency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProficiencyRepository extends ReactiveCrudRepository<Proficiency, Long>,
        ReactiveSortingRepository<Proficiency, Long> {

    Mono<Proficiency> findByNameIgnoreCase(String name);

}
