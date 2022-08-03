package api.service;

import api.domain.Item;
import api.mapper.ItemMapper;
import api.repository.ItemRepository;
import api.request.post.ItemPostRequestBody;
import api.request.put.ItemPutRequestBody;
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

import static api.config.cache.CacheConfiguration.ITEM_CACHE_NAME;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ITEM_CACHE_NAME)
public class ItemService implements Service<Item, ItemPostRequestBody, ItemPutRequestBody> {

    private static final String CACHE_NAME = ITEM_CACHE_NAME;
    private final ItemRepository itemRepository;
    private final ProficiencyService proficiencyService;
    private final CacheService cacheService;

    @Override
    @Cacheable(keyGenerator = "customKeyGenerator")
    public Mono<Item> findById(int id) {
        return itemRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException(null))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o item (id = {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> cacheService.getDurationTTL(), ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Cacheable(keyGenerator = "customKeyGenerator")
    public Mono<Item> findByName(String name) {
        return itemRepository.findByNameIgnoreCase(name)
                .switchIfEmpty(monoResponseStatusNotFoundException(name))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o item (name = {}): {}", name, ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> cacheService.getDurationTTL(), ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Override
    @Cacheable(keyGenerator = "customKeyGenerator")
    public Flux<Item> findAll() {
        return itemRepository.findAll(Sort.by("id"))
                .onErrorResume(ex -> {
                    cacheService.evictCache(CACHE_NAME, "findAll");
                    return Flux.error(ex);
                })
                .cache(cacheService.getDurationTTL());
    }

    @Cacheable(keyGenerator = "customKeyGenerator")
    public Flux<Item> findAllByProficiency(String proficiency) {
        return proficiencyService.findByName(proficiency)
                .flatMapMany(p -> itemRepository.findAllByProficiencyIgnoreCase(p.getName(), Sort.by("id")))
                .onErrorResume(ex -> {
                    cacheService.evictCache(CACHE_NAME, "findAllByProficiency", proficiency);
                    return Flux.error(ex);
                })
                .cache(cacheService.getDurationTTL());
    }

    @Override
    public Mono<Item> save(ItemPostRequestBody itemPostRequestBody) {
        return itemRepository.save(ItemMapper.INSTANCE.toItem(itemPostRequestBody))
                .doOnSuccess(item -> {
                    if (item != null) {
                        cacheService.evictCache(CACHE_NAME, "findAll");
                        if (item.getProficiency() != null)
                            cacheService.evictCache(CACHE_NAME, "findAllByProficiency", item.getProficiency());
                    }
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar o item: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    public Mono<Void> update(ItemPutRequestBody itemPutRequestBody) {
        Item newItem = ItemMapper.INSTANCE.toItem(itemPutRequestBody);
        return findById(newItem.getId())
                .flatMap(oldItem -> itemRepository.save(newItem).thenReturn(oldItem))
                .doOnSuccess(item -> {
                    if (item != null) {
                        cacheService.evictCache(CACHE_NAME, "findById", item.getId());
                        cacheService.evictCache(CACHE_NAME, "findByName", item.getName());
                        cacheService.evictCache(CACHE_NAME, "findAll");
                        if (item.getProficiency() != null)
                            cacheService.evictCache(CACHE_NAME, "findAllByProficiency", item.getProficiency());
                    }
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar o item (id: {}): {}", newItem.getId(), ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    public Mono<Void> delete(int id) {
        return findById(id)
                .flatMap(item -> itemRepository.delete(item).thenReturn(item))
                .doOnSuccess(item -> {
                    if (item != null) {
                        cacheService.evictCache(CACHE_NAME, "findById", item.getId());
                        cacheService.evictCache(CACHE_NAME, "findByName", item.getName());
                        cacheService.evictCache(CACHE_NAME, "findAll");
                        if (item.getProficiency() != null)
                            cacheService.evictCache(CACHE_NAME, "findAllByProficiency", item.getProficiency());
                    }
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao excluir o item (id: {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    private <T> Mono<T> monoResponseStatusNotFoundException(String item) {
        String message = item != null && item.length() > 0 ? String.format("Item '%s' not found", item) : "Item not found";
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

}