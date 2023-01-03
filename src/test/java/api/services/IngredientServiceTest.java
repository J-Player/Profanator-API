package api.services;

import api.configs.BlockHoundTest;
import api.domains.Ingredient;
import api.repositories.IngredientRepository;
import api.services.cache.CacheService;
import api.util.IngredientCreator;
import api.util.ItemCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@DisplayName("Ingredient Service Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private CacheService cacheService;

    private final Ingredient ingredient = IngredientCreator.ingredient();

    @BeforeAll
    public static void blockHound() {
        BlockHound.install();
    }

    @Test
    @Order(-1)
    @DisplayName("[BlockHound] Check if BlockHound is working")
    void blockHoundWorks() {
        BlockHoundTest.test();
    }

    @BeforeEach
    void setUp() {
        BDDMockito.when(ingredientRepository.findById(any(UUID.class)))
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
        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("findById | Returns a mono of ingredient when successful")
    void findById() {
        StepVerifier.create(ingredientService.findById(UUID.randomUUID()))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when ingredient does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.findById(UUID.randomUUID()))
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
        StepVerifier.create(ingredientService.save(ingredient))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Save updated ingredient and returns empty mono when successful")
    void update() {
        StepVerifier.create(ingredientService.update(ingredient))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when ingredient does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.update(ingredient))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | Removes the ingredient when successful")
    void delete() {
        StepVerifier.create(ingredientService.delete(UUID.randomUUID()))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns Mono error when ingredient does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.delete(UUID.randomUUID()))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}