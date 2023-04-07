package api.services;

import api.domains.Item;
import api.repositories.ItemRepository;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Item Service Test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ProficiencyService proficiencyService;

    @Mock
    private CacheService cacheService;

    private final Item item = ItemCreator.item();

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findAll(any(Sort.class)))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.findAllByProficiencyIgnoreCase(anyString(), any(Sort.class)))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.save(any(Item.class)))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.delete(any(Item.class)))
                .thenReturn(Mono.empty());
        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("findById | Returns a mono of item when successful")
    void findById() {
        StepVerifier.create(itemService.findById(1L))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when item does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.findById(1L))
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
        BDDMockito.when(proficiencyService.findByName(anyString()))
                .thenReturn(Mono.just(ProficiencyCreator.proficiency()));
        StepVerifier.create(itemService.findAllByProficiency(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll | Returns a flux of item when successful")
    void findAll() {
        StepVerifier.create(itemService.findAll())
                .expectSubscription()
                .expectNext(item)
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
        BDDMockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.update(item))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | removes the item when successful")
    void delete() {
        StepVerifier.create(itemService.delete(1L))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns mono error when item does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.delete(1L))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}