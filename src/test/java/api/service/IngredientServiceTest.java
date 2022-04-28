package api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import api.domain.Ingredient;
import api.repository.IngredientRepository;
import api.util.IngredientCreator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class IngredientServiceTest {

    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private IngredientRepository ingredientRepository;

    private final Ingredient ingredient = IngredientCreator.createValidIngredient();

    @BeforeEach
    void setUp() {
        BDDMockito.when(ingredientRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.findByItemAndIngredientAllIgnoreCase(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.findByItemIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientRepository.findAll())
                .thenReturn(Flux.just(ingredient));

        //SAVE
        BDDMockito.when(ingredientRepository.save(IngredientCreator.createIngredientToBeSaved()))
                .thenReturn(Mono.just(ingredient));
        //UPDATE
        BDDMockito.when(ingredientRepository.save(IngredientCreator.createIngredientToBeUpdated()))
                .thenReturn(Mono.empty());
        //DELETE
        BDDMockito.when(ingredientRepository.delete(ArgumentMatchers.any(Ingredient.class)))
                .thenReturn(Mono.empty());

    }

    @Test
    @DisplayName("findById returns a mono ingredient when successful")
    void findById_ReturnMonoIngredient_WhenSuccessful() {
        StepVerifier.create(ingredientService.findById(1))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns a mono error when ingredient does not exist")
    void findById_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAllByIngredient returns a Mono ingredient when successful")
    void findAllByIngredient_ReturnMonoIngredient_WhenSuccessful() {
        StepVerifier.create(ingredientService.findAllByItem(""))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findAllByIngredient returns a mono error when ingredient does not exist")
    void findAllByIngredient_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findByItemIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Flux.empty());
        StepVerifier.create(ingredientService.findAllByItem(""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findByItemAndIngredient returns a mono of ingredient when successful")
    void findByItemAndIngredient_ReturnFluxOfIngredient_WhenSuccessful() {
        StepVerifier.create(ingredientService.findByItemAndIngredient("", ""))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByItemAndIngredient returns mono error when ingredient does not exist")
    void findByItemAndIngredient_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findByItemAndIngredientAllIgnoreCase(
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.findByItemAndIngredient("", ""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAll returns a flux of ingredient when successful")
    void findAll_ReturnFluxOfIngredient_WhenSuccessful() {
        StepVerifier.create(ingredientService.findAll())
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("save creates an ingredient when successful")
    void save() {
        StepVerifier.create(ingredientService.save(IngredientCreator.createIngredientToBeSaved()))
                .expectSubscription()
                .expectNext(ingredient)
                .verifyComplete();
    }

    @Test
    @DisplayName("update save updated ingredient and returns empty mono when successful")
    void update() {
        Ingredient ingredientToBeUpdated = IngredientCreator.createIngredientToBeUpdated();
        BDDMockito.when(ingredientRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(ingredientToBeUpdated));
        StepVerifier.create(ingredientService.update(ingredientToBeUpdated))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update returns mono error when ingredient does not exist")
    void update_ReturnMonoError_WhenEmptyMonoIsReturned() {
        Ingredient ingredientToBeUpdated = IngredientCreator.createIngredientToBeUpdated();
        BDDMockito.when(ingredientRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.update(ingredientToBeUpdated))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete removes the ingredient when successful")
    void delete() {
        StepVerifier.create(ingredientService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete returns Mono error when ingredient does not exist")
    void delete_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(ingredientRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(ingredientService.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}