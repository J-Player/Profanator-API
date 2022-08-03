package api.repository;

import api.domain.Ingredient;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends ReactiveSortingRepository<Ingredient, Integer> {

    Mono<Ingredient> findByProductAndNameAllIgnoreCase(String product, String name);

    Flux<Ingredient> findByProductIgnoreCase(String product, Sort sort);

}
