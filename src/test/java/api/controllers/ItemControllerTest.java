package api.controllers;

import api.domains.Item;
import api.domains.dtos.ItemDTO;
import api.services.ItemService;
import api.util.ItemCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@DisplayName("Item Controller Test")
class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private final Item item = ItemCreator.item();
    private final ItemDTO itemDTO = ItemCreator.itemDTO();

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
        BDDMockito.when(itemService.findById(any(UUID.class)))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findByName(anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findAll())
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemService.findAllByProficiency(anyString()))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemService.save(any(Item.class)))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.update(any(Item.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(itemService.delete(any(UUID.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a item when successful")
    void findById() {
        StepVerifier.create(itemController.findById(UUID.randomUUID()))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName | Returns a item when successful")
    void findByName() {
        StepVerifier.create(itemController.findByName(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAll | Returns all items by proficiency when successful")
    void listAll() {
        StepVerifier.create(itemController.listAll(""))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Returns a item when successful")
    void save() {
        StepVerifier.create(itemController.save(itemDTO))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        StepVerifier.create(itemController.update(UUID.randomUUID(), itemDTO))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(itemController.delete(UUID.randomUUID()))
                .expectSubscription()
                .verifyComplete();
    }

}