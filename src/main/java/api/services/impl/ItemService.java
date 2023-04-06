package api.services.impl;

import api.domains.Item;
import api.repositories.ItemRepository;
import api.services.IService;
import api.services.cache.CacheService;
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
import java.util.Optional;

import static api.configs.cache.CacheConfig.ITEM_CACHE_NAME;
import static api.configs.cache.CacheConfig.TTL;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ITEM_CACHE_NAME)
public class ItemService implements IService<Item> {

    private final ItemRepository itemRepository;
    private final ProficiencyService proficiencyService;
    private final CacheService cacheService;

    @Override
    @Cacheable
    public Mono<Item> findById(Long id) {
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
    @CacheEvict(allEntries = true)
    public Mono<Item> save(Item item) {
        return itemRepository.save(item)
                .doOnNext(i -> log.info("Item salvo com sucesso! ({}).", i))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar o item: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Mono<Void> update(Item item) {
        return findById(item.getId())
                .map(oldItem -> {
                    item.setCreatedAt(oldItem.getCreatedAt());
                    item.setUpdatedAt(oldItem.getUpdatedAt());
                    item.setVersion(oldItem.getVersion());
                    return item;
                })
                .flatMap(itemRepository::save)
                .doOnNext(i -> log.info("Item atualizado com sucesso! {}", i))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar o item (id: {}): {}", item.getId(), ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    @CacheEvict(allEntries = true)
    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(item -> itemRepository.delete(item).thenReturn(item))
                .doOnSuccess(item -> log.info("Item excluído com sucesso! ({})", item))
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