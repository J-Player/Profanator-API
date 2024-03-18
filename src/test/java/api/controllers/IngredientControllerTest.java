package api.controllers;

import api.controllers.impl.IngredientController;
import api.models.dtos.IngredientDTO;
import api.models.entities.Ingredient;
import api.services.impl.IngredientService;
import api.util.IngredientCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Ingredient Controller Test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class IngredientControllerTest {

    @InjectMocks
    private IngredientController ingredientController;

    @Mock
    private IngredientService ingredientService;

    private final Ingredient ingredient = IngredientCreator.ingredient();
    private final IngredientDTO ingredientDTO = IngredientCreator.ingredientDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(ingredientService.findById(anyInt()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.findAll(any()))
                .thenReturn(Mono.just(new PageImpl<>(List.of(ingredient))));
        BDDMockito.when(ingredientService.findAllByProduct(anyString(), any()))
                .thenReturn(Mono.just(new PageImpl<>(List.of(ingredient))));
        BDDMockito.when(ingredientService.save(any(Ingredient.class)))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.update(any(Ingredient.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(ingredientService.delete(anyInt()))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
        StepVerifier.create(ingredientController.findById(1))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAll | Returns all ingredients when successful")
    void listAll() {
        StepVerifier.create(ingredientController.listAll(null, PageRequest.of(1, 100)))
                .expectSubscription()
                .expectNext(new PageImpl<>(List.of(ingredient)))
                .verifyComplete();
    }

    @Test
    @DisplayName("listAllByItem | Returns all ingredients of a item when successful")
    void listAllByItem() {
        StepVerifier.create(ingredientController.listAll("", PageRequest.of(1, 100)))
                .expectSubscription()
                .expectNext(new PageImpl<>(List.of(ingredient)))
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
        StepVerifier.create(ingredientController.update(1, ingredientDTO))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(ingredientController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

}