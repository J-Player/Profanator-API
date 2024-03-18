package api.controllers;

<<<<<<< HEAD
import api.controllers.impl.IngredientController;
import api.models.dtos.IngredientDTO;
import api.models.entities.Ingredient;
=======
import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;
>>>>>>> main
import api.services.impl.IngredientService;
import api.utils.IngredientCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
<<<<<<< HEAD
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

=======
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

>>>>>>> main
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
<<<<<<< HEAD
        BDDMockito.when(ingredientService.findById(anyInt()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.findAll(any()))
                .thenReturn(Mono.just(new PageImpl<>(List.of(ingredient))));
        BDDMockito.when(ingredientService.findAllByProduct(anyString(), any()))
                .thenReturn(Mono.just(new PageImpl<>(List.of(ingredient))));
        BDDMockito.when(ingredientService.save(any(Ingredient.class)))
=======
        BDDMockito.when(ingredientService.findById(anyLong()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.findAll())
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientService.findAllByProduct(anyString()))
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientService.save(any(IngredientDTO.class)))
>>>>>>> main
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientService.update(any(IngredientDTO.class), anyLong()))
                .thenReturn(Mono.empty());
<<<<<<< HEAD
        BDDMockito.when(ingredientService.delete(anyInt()))
=======
        BDDMockito.when(ingredientService.delete(anyLong()))
>>>>>>> main
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
<<<<<<< HEAD
        StepVerifier.create(ingredientController.findById(1))
=======
        StepVerifier.create(ingredientController.findById(1L))
>>>>>>> main
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
<<<<<<< HEAD
        StepVerifier.create(ingredientController.update(1, ingredientDTO))
=======
        StepVerifier.create(ingredientController.update(ingredientDTO, 1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
<<<<<<< HEAD
        StepVerifier.create(ingredientController.delete(1))
=======
        StepVerifier.create(ingredientController.delete(1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }

}