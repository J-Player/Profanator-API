package api.controllers;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;
import api.services.impl.IngredientService;
import api.utils.IngredientCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Ingredient Controller Test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class IngredientControllerTest {

    @InjectMocks
    private IngredientController ingredientController;

    @Mock
    private IngredientService ingredientService;

    private final Ingredient ingredient = IngredientCreator.ingredient();
    private final IngredientDTO ingredientDTO = IngredientCreator.ingredientDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(ingredientService.findById(anyLong()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.findAll())
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientService.findAllByProduct(anyString()))
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientService.save(any(IngredientDTO.class)))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.update(any(IngredientDTO.class), anyLong()))
                .thenReturn(Mono.empty());
        BDDMockito.when(ingredientService.delete(anyLong()))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
        StepVerifier.create(ingredientController.findById(1L))
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
        StepVerifier.create(ingredientController.update(ingredientDTO, 1L))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(ingredientController.delete(1L))
                .expectSubscription()
                .verifyComplete();
    }

}