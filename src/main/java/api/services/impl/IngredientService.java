package api.services.impl;

import static api.configs.cache.CacheConfig.INGREDIENT_CACHE_NAME;
import static api.configs.cache.CacheConfig.TTL;

import java.time.Duration;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import api.models.entities.Ingredient;
import api.repositories.impl.IngredientRepository;
import api.services.IService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = INGREDIENT_CACHE_NAME)
public class IngredientService implements IService<Ingredient> {

    private final IngredientRepository ingredientRepository;

    @Override
    @Cacheable
    public Mono<Ingredient> findById(Integer id) {
        return ingredientRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found")))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o Ingredient (id = {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> TTL, ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Override
    @Cacheable
    public Mono<Page<Ingredient>> findAll(Pageable pageable) {
       return ingredientRepository.findAllBy(pageable)
               .collectList()
               .zipWith(ingredientRepository.count())
               .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    @Cacheable
    public Mono<Page<Ingredient>> findAllByProduct(String product, Pageable pageable) {
        return ingredientRepository.findAllByProductIgnoreCase(product, pageable)
                .collectList()
                .zipWith(ingredientRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Mono<Ingredient> save(Ingredient ingredient) {
        return ingredientRepository.save(ingredient)
                .doOnSuccess(i -> log.info("Ingredient salvo com sucesso! ({}).", i))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar o Ingredient: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    @CacheEvict(allEntries = true)
    public Mono<Void> update(Ingredient ingredient) {
        return findById(ingredient.getId())
                .doOnNext(oldIngredient -> {
                    ingredient.setCreatedAt(oldIngredient.getCreatedAt());
                    ingredient.setUpdatedAt(oldIngredient.getUpdatedAt());
                    ingredient.setVersion(oldIngredient.getVersion());
                })
                .flatMap(ingredientRepository::save)
                .doOnSuccess(i -> log.info("Ingredient atualizado com sucesso! {}", i))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar o Ingredient (id: {}): {}", ingredient.getId(), ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    @CacheEvict(allEntries = true)
    public Mono<Void> delete(Integer id) {
        return findById(id)
                .flatMap(ingredient -> ingredientRepository.delete(ingredient).thenReturn(ingredient))
                .doOnSuccess(ingredient -> log.info("Ingredient excluído com sucesso! ({})", ingredient))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao excluir o Ingredient (id: {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    public Mono<Void> deleteAll() {
        return ingredientRepository.deleteAll();
    }

}
