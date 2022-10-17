package api.repositories;

import api.domains.Ingredient;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface IngredientRepository extends ReactiveSortingRepository<Ingredient, UUID> {

    Mono<Ingredient> findByProductAndNameAllIgnoreCase(String product, String name);

    Flux<Ingredient> findByProductIgnoreCase(String product, Sort sort);

}
