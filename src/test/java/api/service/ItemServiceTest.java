package api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import api.domain.Item;
import api.repository.ItemRepository;
import api.util.ItemCreator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    private final Item item = ItemCreator.createValidItem();

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findByNameIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findAll())
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.findAllByProficiencyIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(item));

        //SAVE
        BDDMockito.when(itemRepository.save(ItemCreator.createItemToBeSaved()))
                .thenReturn(Mono.just(item));
        //UPDATE
        BDDMockito.when(itemRepository.save(ItemCreator.createItemToBeUpdated()))
                .thenReturn(Mono.empty());
        //DELETE
        BDDMockito.when(itemRepository.delete(ArgumentMatchers.any(Item.class)))
                .thenReturn(Mono.empty());

    }

    @Test
    @DisplayName("findById returns a mono item when successful")
    void findById_ReturnMonoItem_WhenSuccessful() {
        StepVerifier.create(itemService.findById(1))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns a mono error when item does not exist")
    void findById_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findById(ArgumentMatchers.anyInt()))
                        .thenReturn(Mono.empty());
        StepVerifier.create(itemService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findByName returns a Mono item when successful")
    void findByName_ReturnMonoItem_WhenSuccessful() {
        StepVerifier.create(itemService.findByName(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName returns a mono error when item does not exist")
    void findByName_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findByNameIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.findByName(""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAllByProficiency returns a flux of item when successful")
    void findAllByProficiency_ReturnFluxOfItem_WhenSuccessful() {
        StepVerifier.create(itemService.findAllByProficiency(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll returns a flux of item when successful")
    void findAll_ReturnFluxOfItem_WhenSuccessful() {
        StepVerifier.create(itemService.findAll())
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("save creates an item when successful")
    void save() {
        StepVerifier.create(itemService.save(ItemCreator.createItemToBeSaved()))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("update save updated item and returns empty mono when successful")
    void update() {
        Item itemToBeUpdated = ItemCreator.createItemToBeUpdated();
        BDDMockito.when(itemRepository.findById(ArgumentMatchers.anyInt()))
                        .thenReturn(Mono.just(itemToBeUpdated));
        StepVerifier.create(itemService.update(itemToBeUpdated))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update returns mono error when item does not exist")
    void update_ReturnMonoError_WhenEmptyMonoIsReturned() {
        Item itemToBeUpdated = ItemCreator.createItemToBeUpdated();
        BDDMockito.when(itemRepository.findById(ArgumentMatchers.anyInt()))
                        .thenReturn(Mono.empty());
        StepVerifier.create(itemService.update(itemToBeUpdated))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete removes the item when successful")
    void delete() {
        StepVerifier.create(itemService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete returns Mono error when item does not exist")
    void delete_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}