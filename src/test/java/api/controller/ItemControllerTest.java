package api.controller;

import api.domain.Item;
import api.request.post.ItemPostRequestBody;
import api.request.put.ItemPutRequestBody;
import api.service.ItemService;
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

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Item Controller Test")
class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private final Item item = ItemCreator.item();
    private final ItemPostRequestBody itemToBeSaved = ItemCreator.itemToSave();
    private final ItemPutRequestBody itemToBeUpdated = ItemCreator.itemToUpdate();

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
        BDDMockito.when(itemService.findById(anyInt()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findByName(anyString()))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.findAll())
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemService.findAllByProficiency(anyString()))
                .thenReturn(Flux.just(item));
        BDDMockito.when(itemService.save(any(ItemPostRequestBody.class)))
                .thenReturn(Mono.just(item));
        BDDMockito.when(itemService.update(any(ItemPutRequestBody.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(itemService.delete(anyInt()))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a item when successful")
    void findById() {
        StepVerifier.create(itemController.findById(1))
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
        StepVerifier.create(itemController.save(itemToBeSaved))
                .expectSubscription()
                .expectNext(item)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        StepVerifier.create(itemController.update(itemToBeUpdated))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(itemController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

}