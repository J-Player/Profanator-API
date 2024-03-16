package api.repositories.impl;

import api.models.entities.Item;
import api.repositories.IRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ItemRepository extends IRepository<Item> {

    Mono<Item> findByNameIgnoreCase(String name);

    Flux<Item> findAllByProficiencyIgnoreCase(String proficiency, Pageable pageable);

}
