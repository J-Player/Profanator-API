package api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import api.domain.Ingredient;
import api.repository.IngredientRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IngredientService implements AbstractServiceImpl<Ingredient, Integer> {

    private final IngredientRepository ingredientRepository;

    public Mono<Ingredient> findByItemAndIngredient(String item, String ingredient) {
        return ingredientRepository.findByItemAndIngredientAllIgnoreCase(item, ingredient)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Flux<Ingredient> findAllByItem(String item) {
        return ingredientRepository.findByItemIgnoreCase(item)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Mono<Ingredient> findById(Integer id) {
        return ingredientRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Flux<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Transactional
    public Mono<Ingredient> save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public Mono<Void> update(Ingredient ingredient) {
        return findById(ingredient.getId())
                .flatMap(ingredientRepository::save)
                .then();
    }

    public Mono<Void> delete(Integer id) {
        return findById(id)
                .flatMap(ingredientRepository::delete);
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found"));
    }

}
