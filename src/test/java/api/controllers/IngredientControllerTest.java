package api.controllers;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;
import api.services.IngredientService;
import api.util.IngredientCreator;
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
@DisplayName("Ingredient Controller Test")
class IngredientControllerTest {

    @InjectMocks
    private IngredientController ingredientController;

    @Mock
    private IngredientService ingredientService;

    private final Ingredient ingredient = IngredientCreator.ingredient();
    private final IngredientDTO ingredientDTO = IngredientCreator.ingredientDTO();

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
        BDDMockito.when(ingredientService.findById(any(UUID.class)))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.findByProductAndName(anyString(), anyString()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.findAll())
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientService.findAllByProduct(anyString()))
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientService.save(any(Ingredient.class)))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.update(any(Ingredient.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(ingredientService.delete(any(UUID.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
        StepVerifier.create(ingredientController.findById(UUID.randomUUID()))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByItemAndIngredient | Returns a ingredient when successful")
    void findByItemAndIngredient() {
        StepVerifier.create(ingredientController.findByItemAndIngredient("", ""))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAll | Returns all ingredients when successful")
    void listAll() {
        StepVerifier.create(ingredientController.listAll(null))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAllByItem | Returns all ingredients of a item when successful")
    void listAllByItem() {
        StepVerifier.create(ingredientController.listAll(""))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Returns a ingredient when successful")
    void save() {
        StepVerifier.create(ingredientController.save(ingredientDTO))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        StepVerifier.create(ingredientController.update(UUID.randomUUID(), ingredientDTO))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(ingredientController.delete(UUID.randomUUID()))
                .expectSubscription()
                .verifyComplete();
    }

}