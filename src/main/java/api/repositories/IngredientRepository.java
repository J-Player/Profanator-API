package api.repositories;

import api.domains.Ingredient;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, Long>,
        ReactiveSortingRepository<Ingredient, Long> {

    Mono<Ingredient> findByProductAndNameAllIgnoreCase(String product, String name);

    Flux<Ingredient> findByProductIgnoreCase(String product, Sort sort);

}
