package api.service;

import api.domain.Item;
import api.repository.ItemRepository;
import api.request.post.ItemPostRequestBody;
import api.request.put.ItemPutRequestBody;
import api.util.ItemCreator;
import api.util.ProficiencyCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Item Service Test")
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ProficiencyService proficiencyService;

    @Spy
    private CacheManager cacheManager;

    private final Item item = ItemCreator.item();
    private final ItemPostRequestBody itemToSaved = ItemCreator.itemToSave();
    private final ItemPutRequestBody itemToUpdated = ItemCreator.itemToUpdate();

    @BeforeAll
    public static void blockHound() {
        BlockHound.install();
    }

    @Test
    void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0); //NOSONAR
                return "";
            });
            Schedulers.parallel().schedule(task);
            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemRepository.findById(anyInt()))
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
        StepVerifier.create(itemService.save(itemToSaved))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | save updated item and returns empty mono when successful")
    void update() {
        BDDMockito.when(itemRepository.findById(anyInt()))
                .thenReturn(Mono.just(item));
        StepVerifier.create(itemService.update(itemToUpdated))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when item does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(itemRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(itemService.update(itemToUpdated))
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