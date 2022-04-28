package api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import api.domain.Item;
import api.repository.ItemRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ItemService implements AbstractServiceImpl<Item, Integer> {

    private final ItemRepository itemRepository;

    public Mono<Item> findByName(String item) {
        return itemRepository.findByNameIgnoreCase(item)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Flux<Item> findAllByProficiency(String proficiency) {
        return itemRepository.findAllByProficiencyIgnoreCase(proficiency);
    }

    public Mono<Item> findById(Integer id) {
        return itemRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Flux<Item> findAll() {
        return itemRepository.findAll();
    }

    @Transactional
    public Mono<Item> save(Item item) {
        return itemRepository.save(item);
    }

    public Mono<Void> update(Item item) {
        return findById(item.getId())
                .flatMap(itemRepository::save)
                .then();
    }

    public Mono<Void> delete(Integer id) {
        return findById(id)
                .flatMap(itemRepository::delete);
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

}
