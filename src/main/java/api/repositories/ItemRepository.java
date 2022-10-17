package api.repositories;

import api.domains.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ItemRepository extends ReactiveSortingRepository<Item, UUID> {

    Mono<Item> findByNameIgnoreCase(String name);

    Flux<Item> findAllByProficiencyIgnoreCase(String proficiency, Sort sort);

}
