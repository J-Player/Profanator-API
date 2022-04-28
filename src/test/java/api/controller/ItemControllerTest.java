package api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import api.domain.Item;
import api.service.ItemService;
import api.util.ItemCreator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private final Item item = ItemCreator.createValidItem();

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findAll())
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemService.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.save(ArgumentMatchers.any(Item.class)))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.update(ArgumentMatchers.any(Item.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(itemService.delete(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
    }

    @Test
    void findByName() {
        StepVerifier.create(itemController.findByName(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    void findById() {
        StepVerifier.create(itemController.findById(1))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    void listAll() {
        StepVerifier.create(itemController.listAll())
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    void save() {
        StepVerifier.create(itemController.save(ItemCreator.createItemToBeSaved()))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    void update() {
        StepVerifier.create(itemController.update(ItemCreator.createItemToBeSaved()))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void delete() {
        StepVerifier.create(itemController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

}