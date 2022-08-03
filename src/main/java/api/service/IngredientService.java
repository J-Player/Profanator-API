package api.service;

import api.domain.Ingredient;
import api.mapper.IngredientMapper;
import api.repository.IngredientRepository;
import api.request.post.IngredientPostRequestBody;
import api.request.put.IngredientPutRequestBody;
import api.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static api.config.cache.CacheConfiguration.INGREDIENT_CACHE_NAME;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = INGREDIENT_CACHE_NAME)
public class IngredientService implements Service<Ingredient, IngredientPostRequestBody, IngredientPutRequestBody> {

    private static final String CACHE_NAME = INGREDIENT_CACHE_NAME;
    private final IngredientRepository ingredientRepository;
    private final ItemService itemService;
    private final CacheService cacheService;

    @Override
    @Cacheable(keyGenerator = "customKeyGenerator")
    public Mono<Ingredient> findById(int id) {
        return ingredientRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException())
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o Ingredient: {}", ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> cacheService.getDurationTTL(), ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Cacheable(keyGenerator = "customKeyGenerator")
    public Mono<Ingredient> findByProductAndName(String product, String name) {
        return itemService.findByName(product).zipWith(itemService.findByName(name))
                .flatMap(items -> {
                    String p = items.getT1().getName();
                    String n = items.getT2().getName();
                    return ingredientRepository.findByProductAndNameAllIgnoreCase(p, n);
                })
                .switchIfEmpty(monoResponseStatusNotFoundException())
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o Ingredient [product=\"{}\", name=\"{}\"]: {}", product, name, ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> cacheService.getDurationTTL(), ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Override
    @Cacheable(keyGenerator = "customKeyGenerator")
    public Flux<Ingredient> findAll() {
        return ingredientRepository.findAll(Sort.by("id"))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar os Ingredients: {}", ex.getMessage());
                    cacheService.evictCache(CACHE_NAME, "findAll");
                    return Mono.error(ex);
                })
                .cache(cacheService.getDurationTTL());
    }

    @Cacheable(keyGenerator = "customKeyGenerator")
    public Flux<Ingredient> findAllByProduct(String product) {
        return itemService.findByName(product)
                .flatMapMany(item -> ingredientRepository.findByProductIgnoreCase(item.getName(), Sort.by("id")))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar os Ingredients do item {}: {}", product, ex.getMessage());
                    cacheService.evictCache(CACHE_NAME, "findAllByProduct");
                    return Mono.error(ex);
                })
                .cache(cacheService.getDurationTTL());
    }

    @Override
    public Mono<Ingredient> save(IngredientPostRequestBody ingredientPostRequestBody) {
        return ingredientRepository.save(IngredientMapper.INSTANCE.toIngredient(ingredientPostRequestBody))
                .doOnSuccess(ingredient -> {
                    if (ingredient != null) {
                        cacheService.evictCache(CACHE_NAME, "findAll");
                        cacheService.evictCache(CACHE_NAME, "findAllByProduct", ingredient.getProduct());
                    }
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar o Ingredient: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    public Mono<Void> update(IngredientPutRequestBody ingredientPutRequestBody) {
        Ingredient newIngredient = IngredientMapper.INSTANCE.toIngredient(ingredientPutRequestBody);
        return findById(newIngredient.getId())
                .flatMap(oldIngredient -> ingredientRepository.save(newIngredient).thenReturn(oldIngredient))
                .doOnSuccess(ingredient -> {
                    if (ingredient != null) {
                        cacheService.evictCache(CACHE_NAME, "findById", ingredient.getId());
                        cacheService.evictCache(CACHE_NAME, "findByProductAndName", ingredient.getProduct(), ingredient.getName());
                        cacheService.evictCache(CACHE_NAME, "findAll");
                        cacheService.evictCache(CACHE_NAME, "findAllByProduct", ingredient.getProduct());
                    }
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar o Ingredient (id: {}): {}", newIngredient.getId(), ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    public Mono<Void> delete(int id) {
        return findById(id)
                .doOnNext(ingredientRepository::delete)
                .doOnSuccess(ingredient -> {
                    if (ingredient != null) {
                        cacheService.evictCache(CACHE_NAME, "findById", ingredient.getId());
                        cacheService.evictCache(CACHE_NAME, "findByProductAndName", ingredient.getProduct(), ingredient.getName());
                        cacheService.evictCache(CACHE_NAME, "findAll");
                        cacheService.evictCache(CACHE_NAME, "findAllByProduct", ingredient.getProduct());
                    }
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao excluir o Ingredient (id: {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        String message = "Ingredient not found";
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

}
