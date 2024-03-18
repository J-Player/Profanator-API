package api.repositories;

import api.domains.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ItemRepository extends ReactiveCrudRepository<Item, Long>,
        ReactiveSortingRepository<Item, Long> {

    Mono<Item> findByNameIgnoreCase(String name);

    Flux<Item> findAllByProficiencyIgnoreCase(String proficiency, Sort sort);

}
