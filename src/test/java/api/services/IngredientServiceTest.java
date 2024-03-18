package api.services;

import api.models.entities.Ingredient;
import api.repositories.impl.IngredientRepository;
import api.services.cache.CacheService;
import api.services.impl.IngredientService;
import api.services.impl.ItemService;
import api.util.IngredientCreator;
import api.util.ItemCreator;
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
@DisplayName("Ingredient Service Test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private CacheService cacheService;

    private final Ingredient ingredient = IngredientCreator.ingredient().withId(1);

    @BeforeEach
    void setUp() {
        BDDMockito.when(ingredientRepository.findById(anyInt()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.findByProductAndNameAllIgnoreCase(anyString(), anyString()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.findAllByProductIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientRepository.findAllBy(any(Pageable.class)))
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientRepository.count())
                .thenReturn(Mono.just(1L));
        BDDMockito.when(ingredientRepository.save(any(Ingredient.class)))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.delete(any(Ingredient.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(itemService.findByName(anyString()))
                .thenReturn(Mono.just(ItemCreator.item()));
        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any(Pageable.class));
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
    @DisplayName("findAllByProduct | Returns a flux of ingredient when successful")
    void findAllByProduct() {
        Pageable pageRequest = Pageable.ofSize(100);
        StepVerifier.create(ingredientService.findAllByProduct("", pageRequest))
                .expectSubscription()
                .expectNext(new PageImpl<>(List.of(ingredient), pageRequest, 1))
                .verifyComplete();
    }

    @Test
    @DisplayName("findAll | Returns a flux of ingredient when successful")
    void findAll() {
        Pageable pageable = Pageable.ofSize(100);
        StepVerifier.create(ingredientService.findAll(pageable))
                .expectSubscription()
                .expectNext(new PageImpl<>(List.of(ingredient), pageable, 1))
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
        BDDMockito.when(ingredientRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.update(ingredient))
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