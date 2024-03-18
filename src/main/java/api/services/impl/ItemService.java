package api.services.impl;

<<<<<<< HEAD
import api.models.entities.Item;
import api.repositories.impl.ItemRepository;
=======
import api.domains.Item;
import api.domains.dtos.ItemDTO;
import api.repositories.ItemRepository;
>>>>>>> main
import api.services.IService;
import api.services.cache.CacheService;
import api.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import reactor.core.publisher.Mono;

import java.time.Duration;

import static api.configs.cache.CacheConfig.ITEM_CACHE_NAME;
import static api.configs.cache.CacheConfig.TTL;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ITEM_CACHE_NAME)
public class ItemService implements IService<Item, ItemDTO> {

    private final ItemRepository itemRepository;
    private final ProficiencyService proficiencyService;
    private final CacheService cacheService;

    @Override
    @Cacheable
<<<<<<< HEAD
    public Mono<Item> findById(Integer id) {
=======
    public Mono<Item> findById(Long id) {
>>>>>>> main
        return itemRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException())
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar o item (id = {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(item -> TTL, ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Cacheable
    public Mono<Item> findByName(String name) {
        return itemRepository.findByNameIgnoreCase(name)
                .switchIfEmpty(monoResponseStatusNotFoundException())
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
<<<<<<< HEAD
                .flatMapMany(p -> itemRepository.findAllByProficiencyIgnoreCase(p.getName(), pageable))
                .collectList()
                .zipWith(itemRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
=======
                .flatMapMany(p -> itemRepository.findAllByProficiencyIgnoreCase(p.getName(), Sort.by("id")))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar os Items da proficiency {}: {}", proficiency, ex.getMessage());
                    cacheService.evictCache(ITEM_CACHE_NAME, "findAllByProficiency", proficiency);
                    return Flux.error(ex);
                })
                .cache(TTL);
>>>>>>> main
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Mono<Item> save(ItemDTO itemDTO) {
        return itemRepository.save(MapperUtil.MAPPER.map(itemDTO, Item.class))
                .doOnNext(i -> log.info("Item salvo com sucesso! ({}).", i))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar o item: {}", ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Mono<Void> update(ItemDTO itemDTO, Long id) {
        return findById(id)
                .doOnNext(item -> MapperUtil.MAPPER.map(itemDTO, item))
                .flatMap(item -> itemRepository.save(item)
                        .doOnSuccess(i -> log.info("Item atualizado com sucesso! {}", i))
                        .onErrorResume(ex -> {
                            log.error("Ocorreu um erro durante a atualização de item: {}", ex.getMessage());
                            return Mono.error(ex);
                        }))
                .then();
    }

    @Override
    @CacheEvict(allEntries = true)
<<<<<<< HEAD
    public Mono<Void> delete(Integer id) {
=======
    public Mono<Void> delete(Long id) {
>>>>>>> main
        return findById(id)
                .flatMap(item -> itemRepository.delete(item).thenReturn(item))
                .doOnSuccess(item -> log.info("Item excluído com sucesso! ({})", item))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao excluir o item (id: {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

<<<<<<< HEAD
    public Mono<Void> deleteAll() {
        return itemRepository.deleteAll();
    }

    private <T> Mono<T> monoResponseStatusNotFoundException(String item) {
        String message = item != null && item.length() > 0 ? String.format("Item '%s' not found", item) : "Item not found";
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
=======
    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
>>>>>>> main
    }

}