package api.services;

import api.domains.Item;
import api.repositories.ItemRepository;
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

import static api.configs.cache.CacheConfig.ITEM_CACHE_NAME;
import static api.configs.cache.CacheConfig.TTL;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ITEM_CACHE_NAME)
public class ItemService implements AbstractService<Item> {

    private final ItemRepository itemRepository;
    private final ProficiencyService proficiencyService;
    private final CacheService cacheService;

    @Override
    @Cacheable
    public Mono<Item> findById(UUID id) {
        return itemRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException(null))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o item (id = {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> TTL, ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Cacheable
    public Mono<Item> findByName(String name) {
        return itemRepository.findByNameIgnoreCase(name)
                .switchIfEmpty(monoResponseStatusNotFoundException(name))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o item (name = {}): {}", name, ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> TTL, ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Override
    @Cacheable
    public Flux<Item> findAll() {
        return itemRepository.findAll(Sort.by("id"))
                .onErrorResume(ex -> {
                    cacheService.evictCache(ITEM_CACHE_NAME);
                    return Flux.error(ex);
                })
                .cache(TTL);
    }

    @Cacheable
    public Flux<Item> findAllByProficiency(String proficiency) {
        return proficiencyService.findByName(proficiency)
                .flatMapMany(p -> itemRepository.findAllByProficiencyIgnoreCase(p.getName(), Sort.by("id")))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar os Items da proficiency {}: {}", proficiency, ex.getMessage());
                    StackWalker walker = StackWalker.getInstance();
                    Optional<String> optional = walker.walk(frames -> frames
                            .findFirst()
                            .map(StackWalker.StackFrame::getMethodName));
                    optional.ifPresent(methodName -> cacheService.evictCache(ITEM_CACHE_NAME, methodName, proficiency));
                    return Flux.error(ex);
                })
                .cache(TTL);
    }

    @Override
    @Transactional
    public Mono<Item> save(Item item) {
        return itemRepository.save(item)
                .doOnNext(i -> {
                    log.info("Item salvo com sucesso! ({}).", i);
                    cacheService.evictCache(ITEM_CACHE_NAME, "findAll");
                    if (i.getProficiency() != null)
                        cacheService.evictCache(ITEM_CACHE_NAME, "findAllByProficiency", i.getProficiency());
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar o item: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    public Mono<Void> update(Item item) {
        return findById(item.getId())
                .doOnNext(oldItem -> {
                    item.setCreatedAt(oldItem.getCreatedAt());
                    item.setUpdatedAt(oldItem.getUpdatedAt());
                    item.setVersion(oldItem.getVersion());
                })
                .flatMap(oldItem -> itemRepository.save(item)
                        .doOnNext(i -> log.info("Item atualizado com sucesso! {}", i))
                        .thenReturn(oldItem))
                .doOnNext(oldItem -> {
                    cacheService.evictCache(ITEM_CACHE_NAME, "findById", oldItem.getId());
                    cacheService.evictCache(ITEM_CACHE_NAME, "findByName", oldItem.getName());
                    cacheService.evictCache(ITEM_CACHE_NAME, "findAll");
                    if (oldItem.getProficiency() != null)
                        cacheService.evictCache(ITEM_CACHE_NAME, "findAllByProficiency", oldItem.getProficiency());
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar o item (id: {}): {}", item.getId(), ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return findById(id)
                .flatMap(item -> itemRepository.delete(item).thenReturn(item))
                .doOnNext(item -> {
                    log.info("Item excluído com sucesso! ({})", item);
                    cacheService.evictCache(ITEM_CACHE_NAME, "findById", item.getId());
                    cacheService.evictCache(ITEM_CACHE_NAME, "findByName", item.getName());
                    cacheService.evictCache(ITEM_CACHE_NAME, "findAll");
                    if (item.getProficiency() != null)
                        cacheService.evictCache(ITEM_CACHE_NAME, "findAllByProficiency", item.getProficiency());
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