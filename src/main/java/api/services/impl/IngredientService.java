package api.services.impl;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;
import api.repositories.IngredientRepository;
import api.services.IService;
import api.services.cache.CacheService;
import api.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static api.configs.cache.CacheConfig.INGREDIENT_CACHE_NAME;
import static api.configs.cache.CacheConfig.TTL;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = INGREDIENT_CACHE_NAME)
public class IngredientService implements IService<Ingredient, IngredientDTO> {

    private final IngredientRepository ingredientRepository;
    private final CacheService cacheService;

    @Override
    @Cacheable
    public Mono<Ingredient> findById(Long id) {
        return ingredientRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException())
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o Ingredient (id = {}): {}", id, ex.getMessage());
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
        return ingredientRepository.findByProductIgnoreCase(product, Sort.by("product", "name"))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar os Ingredients do item {}: {}", product, ex.getMessage());
                    cacheService.evictCache(INGREDIENT_CACHE_NAME, "findAllByProduct", product);
                    return Mono.error(ex);
                })
                .cache(TTL);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Mono<Ingredient> save(IngredientDTO ingredientDTO) {
        return ingredientRepository.save(MapperUtil.MAPPER.map(ingredientDTO, Ingredient.class))
                .doOnSuccess(i -> log.info("Ingredient salvo com sucesso! ({}).", i))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar o Ingredient: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Mono<Void> update(IngredientDTO ingredientDTO, Long id) {
        return findById(id)
                .doOnNext(ingredient -> MapperUtil.MAPPER.map(ingredientDTO, ingredient))
                .flatMap(ingredient -> ingredientRepository.save(ingredient)
                        .doOnSuccess(i -> log.info("Ingredient atualizado com sucesso! {}", i))
                        .onErrorResume(ex -> {
                            log.error("Ocorreu um erro ao atualizar o Ingredient (id: {}): {}", ingredient.getId(), ex.getMessage());
                            return Mono.error(ex);
                        }))
                .then();
    }

    @Override
    @CacheEvict(allEntries = true)
    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(ingredient -> ingredientRepository.delete(ingredient).thenReturn(ingredient))
                .doOnSuccess(ingredient -> log.info("Ingredient excluído com sucesso! ({})", ingredient))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao excluir o Ingredient (id: {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found"));
    }

}
