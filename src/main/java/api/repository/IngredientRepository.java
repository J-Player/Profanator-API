package api.repository;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import api.domain.Ingredient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IngredientRepository extends ReactiveSortingRepository<Ingredient, Integer> {

    Mono<Ingredient> findByItemAndIngredientAllIgnoreCase(String item, String ingredient);

    Flux<Ingredient> findByItemIgnoreCase(String item);

}
