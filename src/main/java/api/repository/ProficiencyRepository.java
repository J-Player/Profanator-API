package api.repository;

import api.domain.Proficiency;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProficiencyRepository extends ReactiveSortingRepository<Proficiency, Integer> {

    Mono<Proficiency> findByNameIgnoreCase(String name);

}
