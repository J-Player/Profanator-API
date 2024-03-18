package api.services;

import api.models.entities.Item;
import api.repositories.impl.ItemRepository;
import api.services.cache.CacheService;
import api.services.impl.ItemService;
import api.services.impl.ProficiencyService;
import api.util.ItemCreator;
import api.util.ProficiencyCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Item Service Test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ProficiencyService proficiencyService;

    @Mock
    private CacheService cacheService;

    private final Item item = ItemCreator.item().withId(1);

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemRepository.findById(anyInt()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findAllBy(any(Pageable.class)))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.count())
                .thenReturn(Mono.just(1L));
        BDDMockito.when(itemRepository.findAllByProficiencyIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.save(any(Item.class)))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.delete(any(Item.class)))
                .thenReturn(Mono.empty());
        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    @DisplayName("findById | Returns a mono of item when successful")
    void findById() {
        StepVerifier.create(itemService.findById(1))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when item does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findByName | Returns a mono of item when successful")
    void findByName() {
        StepVerifier.create(itemService.findByName(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName | Returns a mono error when item does not exists")
    void findByName_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.findByName(""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAllByProficiency | Returns a flux of item when successful")
    void findAllByProficiency() {
        Pageable pageable = Pageable.ofSize(100);
        List<Item> itemList = List.of(item);
        BDDMockito.when(proficiencyService.findByName(anyString()))
                .thenReturn(Mono.just(ProficiencyCreator.proficiency()));
        StepVerifier.create(itemService.findAllByProficiency("", pageable))
                .expectSubscription()
                .expectNext(new PageImpl<>(itemList, pageable, itemList.size()))
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll | Returns a flux of item when successful")
    void findAll() {
        Pageable pageable = Pageable.ofSize(100);
        List<Item> itemList = List.of(item);
        StepVerifier.create(itemService.findAll(pageable))
                .expectSubscription()
                .expectNext(new PageImpl<>(itemList, pageable, itemList.size()))
                .verifyComplete();
    }

    @Test
    @DisplayName("save | creates an item when successful")
    void save() {
        StepVerifier.create(itemService.save(item))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | save updated item and returns empty mono when successful")
    void update() {
        StepVerifier.create(itemService.update(item))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when item does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.update(item))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | removes the item when successful")
    void delete() {
        StepVerifier.create(itemService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns mono error when item does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}