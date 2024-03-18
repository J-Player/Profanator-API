package api.services;

<<<<<<< HEAD
import api.models.entities.Ingredient;
import api.repositories.impl.IngredientRepository;
=======
import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;
import api.repositories.IngredientRepository;
>>>>>>> main
import api.services.cache.CacheService;
import api.services.impl.IngredientService;
import api.services.impl.ItemService;
import api.utils.IngredientCreator;
import api.utils.ItemCreator;
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

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> main
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Ingredient Service Test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private CacheService cacheService;

<<<<<<< HEAD
    private final Ingredient ingredient = IngredientCreator.ingredient().withId(1);

    @BeforeEach
    void setUp() {
        BDDMockito.when(ingredientRepository.findById(anyInt()))
=======
    private final Ingredient ingredient = IngredientCreator.ingredient();
    private final IngredientDTO ingredientDTO = IngredientCreator.ingredientDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(ingredientRepository.findById(anyLong()))
>>>>>>> main
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
<<<<<<< HEAD
        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any(Pageable.class));
=======
        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any());
>>>>>>> main
    }

    @Test
    @DisplayName("findById | Returns a mono of ingredient when successful")
    void findById() {
<<<<<<< HEAD
        StepVerifier.create(ingredientService.findById(1))
=======
        StepVerifier.create(ingredientService.findById(1L))
>>>>>>> main
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when ingredient does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
<<<<<<< HEAD
        BDDMockito.when(ingredientRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.findById(1))
=======
        BDDMockito.when(ingredientRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.findById(1L))
>>>>>>> main
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
        StepVerifier.create(ingredientService.save(ingredientDTO))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Save updated ingredient and returns empty mono when successful")
    void update() {
        StepVerifier.create(ingredientService.update(ingredientDTO, 1L))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when ingredient does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
<<<<<<< HEAD
        BDDMockito.when(ingredientRepository.findById(anyInt()))
=======
        BDDMockito.when(ingredientRepository.findById(anyLong()))
>>>>>>> main
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.update(ingredientDTO, 1L))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | Removes the ingredient when successful")
    void delete() {
<<<<<<< HEAD
        StepVerifier.create(ingredientService.delete(1))
=======
        StepVerifier.create(ingredientService.delete(1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns Mono error when ingredient does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
<<<<<<< HEAD
        BDDMockito.when(ingredientRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.delete(1))
=======
        BDDMockito.when(ingredientRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.delete(1L))
>>>>>>> main
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}