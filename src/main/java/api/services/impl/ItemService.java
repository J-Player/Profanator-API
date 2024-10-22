package api.services.impl;

import static api.configs.cache.CacheConfig.ITEM_CACHE_NAME;
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

import api.models.entities.Item;
import api.repositories.impl.ItemRepository;
import api.services.IService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ITEM_CACHE_NAME)
public class ItemService implements IService<Item> {

    private final ItemRepository itemRepository;
    private final ProficiencyService proficiencyService;

    @Override
    @Cacheable
    public Mono<Item> findById(Integer id) {
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
    public Mono<Page<Item>> findAll(Pageable pageable) {
        return itemRepository.findAllBy(pageable)
                .collectList()
                .zipWith(itemRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    public Mono<Page<Item>> findAllByProficiency(String proficiency, Pageable pageable) {
        return proficiencyService.findByName(proficiency)
                .flatMapMany(p -> itemRepository.findAllByProficiencyIgnoreCase(p.getName(), pageable))
                .collectList()
                .zipWith(itemRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
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
    @CacheEvict(allEntries = true)
    public Mono<Void> update(Item item) {
        return findById(item.getId())
                .doOnNext(oldItem -> {
                    item.setCreatedAt(oldItem.getCreatedAt());
                    item.setUpdatedAt(oldItem.getUpdatedAt());
                    item.setVersion(oldItem.getVersion());
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
    public Mono<Void> delete(Integer id) {
        return findById(id)
                .flatMap(item -> itemRepository.delete(item).thenReturn(item))
                .doOnSuccess(item -> log.info("Item excluído com sucesso! ({})", item))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao excluir o item (id: {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    public Mono<Void> deleteAll() {
        return itemRepository.deleteAll();
    }

    private <T> Mono<T> monoResponseStatusNotFoundException(String item) {
        String message = item != null && item.length() > 0 ? String.format("Item '%s' not found", item) : "Item not found";
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

}