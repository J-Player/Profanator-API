package api.services;

import api.domains.Ingredient;
import api.repositories.IngredientRepository;
import api.services.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static api.configs.cache.CacheConfig.INGREDIENT_CACHE_NAME;
import static api.configs.cache.CacheConfig.TTL;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = INGREDIENT_CACHE_NAME)
public class IngredientService implements AbstractService<Ingredient> {

    private final IngredientRepository ingredientRepository;
    private final ItemService itemService;
    private final CacheService cacheService;

    @Override
    @Cacheable
    public Mono<Ingredient> findById(UUID id) {
        return ingredientRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException())
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o Ingredient (id = {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> TTL, ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Cacheable
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
                .cache(item -> TTL, ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Override
    @Cacheable
    public Flux<Ingredient> findAll() {
        return ingredientRepository.findAll(Sort.by("product", "name"))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar os Ingredients: {}", ex.getMessage());
                    cacheService.evictCache(INGREDIENT_CACHE_NAME);
                    return Mono.error(ex);
                })
                .cache(TTL);
    }

    @Cacheable
    public Flux<Ingredient> findAllByProduct(String product) {
        return itemService.findByName(product)
                .flatMapMany(item -> ingredientRepository.findByProductIgnoreCase(item.getName(), Sort.by("product", "name")))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar os Ingredients do item {}: {}", product, ex.getMessage());
                    StackWalker walker = StackWalker.getInstance();
                    Optional<String> optional = walker.walk(frames -> frames
                            .findFirst()
                            .map(StackWalker.StackFrame::getMethodName));
                    optional.ifPresent(methodName -> cacheService.evictCache(INGREDIENT_CACHE_NAME, methodName, product));
                    return Mono.error(ex);
                })
                .cache(TTL);
    }

    @Override
    @Transactional
    public Mono<Ingredient> save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient)
                .doOnNext(i -> {
                    log.info("Ingredient salvo com sucesso! ({}).", i);
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findAll");
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findAllByProduct", i.getProduct());
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar o Ingredient: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    public Mono<Void> update(Ingredient ingredient) {
        return findById(ingredient.getId())
                .doOnNext(oldIngredient -> {
                    ingredient.setCreatedAt(oldIngredient.getCreatedAt());
                    ingredient.setUpdatedAt(oldIngredient.getUpdatedAt());
                    ingredient.setVersion(oldIngredient.getVersion());
                })
                .flatMap(oldIngredient -> ingredientRepository.save(ingredient)
                        .doOnNext(i -> log.info("Ingredient atualizado com sucesso! {}", i))
                        .thenReturn(oldIngredient))
                .doOnNext(i -> {
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findById", i.getId());
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findByProductAndName", i.getProduct(), i.getName());
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findAll");
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findAllByProduct", i.getProduct());
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar o Ingredient (id: {}): {}", ingredient.getId(), ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return findById(id)
                .doOnNext(ingredientRepository::delete)
                .doOnSuccess(ingredient -> {
                    log.info("Ingredient excluído com sucesso! ({})", ingredient);
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findById", ingredient.getId());
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findByProductAndName", ingredient.getProduct(), ingredient.getName());
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findAll");
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findAllByProduct", ingredient.getProduct());
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
