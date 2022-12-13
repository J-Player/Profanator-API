package api.integration;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;
import api.integration.annotation.IntegrationTest;
import api.repositories.IngredientRepository;
import api.services.ItemService;
import api.util.IngredientCreator;
import api.util.ItemCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@IntegrationTest
@DisplayName("Ingredient Controller Integration Test")
class IngredientControllerIT {

    private static final String ADMIN_USER = "admin";
    private static final String PATH = "/ingredients";

    @Autowired
    private WebTestClient client;

    @MockBean
    private ItemService itemService;

    @MockBean
    private IngredientRepository ingredientRepository;

    private final Ingredient ingredient = IngredientCreator.ingredient();
    private final IngredientDTO ingredientDTO = IngredientCreator.ingredientDTO();
    private final IngredientDTO invalidIngredientDTO = IngredientCreator.invalidIngredientDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(ingredientRepository.findById(any(UUID.class))).thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.findByProductAndNameAllIgnoreCase(anyString(), anyString())).thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.findByProductIgnoreCase(anyString(), any(Sort.class))).thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientRepository.findAll(any(Sort.class))).thenReturn(Flux.just(ingredient));
        BDDMockito.when(ingredientRepository.save(any(Ingredient.class))).thenReturn(Mono.just(ingredient));
        BDDMockito.when(ingredientRepository.delete(any(Ingredient.class))).thenReturn(Mono.empty());
        BDDMockito.when(itemService.findByName(anyString())).thenReturn(Mono.just(ItemCreator.item()));
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
        client.get()
                .uri(PATH.concat("/{id}"), ingredient.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ingredient.class)
                .isEqualTo(ingredient);
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        BDDMockito.when(ingredientRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.get()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("findByItemAndIngredient | Returns a ingredient when successful")
    void findByItemAndIngredient() {
        client.get()
                .uri(PATH.concat("/{item}/{ingredient}"), ingredient.getProduct(), ingredient.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ingredient.class)
                .isEqualTo(ingredient);
    }

    @Test
    @WithUserDetails
    @DisplayName("findByItemAndIngredient | Returns 404 error when not found")
    void findByItemAndIngredient_ReturnsError_WhenNotFound() {
        BDDMockito.when(ingredientRepository.findByProductAndNameAllIgnoreCase(anyString(), anyString()))
                .thenReturn(Mono.empty());
        client.get()
                .uri(PATH.concat("/{item}/{ingredient}"), "randomProduct", "randomName")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("listAll | Returns all ingredients when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Ingredient.class)
                .hasSize(1)
                .contains(ingredient);
    }

    @Test
    @WithUserDetails
    @DisplayName("listAllByItem | Returns all ingredients of a item when successful")
    void listAllByItem() {
        client.get()
                .uri(builder -> builder.path(PATH.concat("/all"))
                        .queryParam("item", ingredient.getProduct()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Ingredient.class)
                .hasSize(1)
                .contains(ingredient);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns a ingredient when successful")
    void save() {
        client.post()
                .uri(PATH)
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Ingredient.class)
                .isEqualTo(ingredient);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns 400 error when invalid ingredient")
    void save_ReturnsError_WhenInvalidIngredient() {
        client.post()
                .uri(PATH)
                .bodyValue(invalidIngredientDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("save | Returns 403 error when forbidden user")
    void save_ReturnsError_WhenForbiddenUser() {
        client.post()
                .uri(PATH)
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid ingredient")
    void update_ReturnsError_WhenInvalidIngredient() {
        BDDMockito.when(ingredientRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(invalidIngredientDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        client.delete()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails
    @DisplayName("delete | Returns 403 error when forbidden user")
    void delete_ReturnsError_WhenForbiddenUser() {
        client.delete()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        BDDMockito.when(ingredientRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.delete()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

}
