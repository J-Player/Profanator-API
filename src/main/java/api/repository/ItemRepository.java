package api.repository;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import api.domain.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository extends ReactiveSortingRepository<Item, Integer> {

    Mono<Item> findByNameIgnoreCase(String name);

    Flux<Item> findAllByProficiencyIgnoreCase(String proficiency);

}
