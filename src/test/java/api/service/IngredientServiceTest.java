package api.service;

import api.domain.Ingredient;
import api.repository.IngredientRepository;
import api.request.post.IngredientPostRequestBody;
import api.request.put.IngredientPutRequestBody;
import api.util.IngredientCreator;
import api.util.ItemCreator;
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
@DisplayName("Ingredient Service Test")
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ItemService itemService;

    @Spy
    private CacheManager cacheManager;

    private final Ingredient ingredient = IngredientCreator.ingredient();
    private final IngredientPostRequestBody ingredientPostRequestBody = IngredientCreator.ingredientToSave();
    private final IngredientPutRequestBody ingredientToBeUpdated = IngredientCreator.ingredientToUpdate();

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
        BDDMockito.when(ingredientRepository.findById(anyInt()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.findByProductAndNameAllIgnoreCase(anyString(), anyString()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.findByProductIgnoreCase(anyString(), any(Sort.class)))
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientRepository.findAll(any(Sort.class)))
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientRepository.save(any(Ingredient.class)))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.delete(any(Ingredient.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(itemService.findByName(anyString()))
                .thenReturn(Mono.just(ItemCreator.item()));
    }

    @Test
    @DisplayName("findById | Returns a mono of ingredient when successful")
    void findById() {
        StepVerifier.create(ingredientService.findById(1))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when ingredient does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findByProductAndName | Returns a mono of ingredient when successful")
    void findByProductAndName() {
        StepVerifier.create(ingredientService.findByProductAndName("", ""))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByProductAndName | Returns mono error when ingredient does not exists")
    void findByProductAndName_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findByProductAndNameAllIgnoreCase(
                        anyString(), anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.findByProductAndName("", ""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAllByProduct | Returns a flux of ingredient when successful")
    void findAllByProduct() {
        StepVerifier.create(ingredientService.findAllByProduct(""))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll | Returns a flux of ingredient when successful")
    void findAll() {
        StepVerifier.create(ingredientService.findAll())
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Creates an ingredient when successful")
    void save() {
        StepVerifier.create(ingredientService.save(ingredientPostRequestBody))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Save updated ingredient and returns empty mono when successful")
    void update() {
        StepVerifier.create(ingredientService.update(ingredientToBeUpdated))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when ingredient does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.update(ingredientToBeUpdated))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | Removes the ingredient when successful")
    void delete() {
        StepVerifier.create(ingredientService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns Mono error when ingredient does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}