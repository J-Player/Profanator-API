package api.repository;

import api.domain.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ItemRepository extends ReactiveSortingRepository<Item, Integer> {

    Mono<Item> findByNameIgnoreCase(String name);

    Flux<Item> findAllByProficiencyIgnoreCase(String proficiency, Sort sort);

}
