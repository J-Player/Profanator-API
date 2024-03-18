package api.repositories.impl;

import api.models.entities.Ingredient;
import api.repositories.IRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends IRepository<Ingredient> {

    Mono<Ingredient> findByProductAndNameAllIgnoreCase(String product, String name);

    Flux<Ingredient> findAllByProductIgnoreCase(String product, Pageable pageable);

}
