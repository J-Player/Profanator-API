package api.repositories.impl;

import api.models.entities.Proficiency;
import api.repositories.IRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProficiencyRepository extends IRepository<Proficiency> {

    Mono<Proficiency> findByNameIgnoreCase(String name);

}
