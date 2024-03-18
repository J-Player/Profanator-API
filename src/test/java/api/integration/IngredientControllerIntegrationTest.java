package api.integration;

import api.integration.annotation.IntegrationTest;
import api.mappers.IngredientMapper;
import api.models.dtos.IngredientDTO;
import api.models.entities.Ingredient;
import api.services.impl.IngredientService;
import api.services.impl.ItemService;
import api.services.impl.ProficiencyService;
import api.util.IngredientCreator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static api.integration.constraint.IntegrationConstraints.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraints.PATH_INGREDIENTS;
import static api.util.ItemCreator.*;
import static api.util.ProficiencyCreator.proficiency;

@IntegrationTest
@DisplayName("Ingredient Controller Integration Test")
class IngredientControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

    private static final IngredientDTO ingredientDTO = IngredientCreator.ingredientDTO();
    private static final IngredientDTO invalidIngredientDTO = IngredientCreator.invalidIngredientDTO();
    private static final Ingredient ingredientToRead = IngredientCreator.ingredientToRead();
    private static final Ingredient ingredientToUpdate = IngredientCreator.ingredientToUpdate();
    private static final Ingredient ingredientToDelete = IngredientCreator.ingredientToDelete();

    @BeforeAll
    static void beforeAll(@Autowired ProficiencyService proficiencyService,
                          @Autowired ItemService itemService,
                          @Autowired IngredientService ingredientService) {
        proficiencyService.save(proficiency())
                .thenMany(Flux.just(item(), itemToRead(), item().withName(ingredientDTO.getName()),
                        itemToUpdate(), itemToDelete()))
                .flatMapSequential(itemService::save)
                .thenMany(Flux.just(ingredientToRead, ingredientToUpdate, ingredientToDelete))
                .flatMapSequential(ingredient -> ingredientService.save(ingredient)
                        .doOnNext(i -> ingredient.setId(i.getId())))
                .blockLast(Duration.ofSeconds(5));
    }

    @AfterAll
    static void afterAll(@Autowired ProficiencyService proficiencyService,
                         @Autowired ItemService itemService,
                         @Autowired IngredientService ingredientService) {
        ingredientService.deleteAll()
                .concatWith(itemService.deleteAll()
                        .concatWith(proficiencyService.deleteAll()))
                .blockLast(Duration.ofSeconds(5));
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
        client.get()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToRead.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ingredient.class);
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
                .uri(PATH_INGREDIENTS.concat("/{id}"), 123)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("listAll | Returns all ingredients when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH_INGREDIENTS.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Ingredient.class);
    }

    @Test
    @WithUserDetails
    @DisplayName("listAllByItem | Returns all ingredients of a item when successful")
    void listAllByItem() {
        client.get()
                .uri(builder -> builder.path(PATH_INGREDIENTS.concat("/all"))
                        .queryParam("item", ingredientToRead.getProduct()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Ingredient.class);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns a ingredient when successful")
    void save() {
        client.post()
                .uri(PATH_INGREDIENTS)
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Ingredient.class);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns 400 error when invalid ingredient")
    void save_ReturnsError_WhenInvalidIngredient() {
        client.post()
                .uri(PATH_INGREDIENTS)
                .bodyValue(invalidIngredientDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("save | Returns 403 error when forbidden user")
    void save_ReturnsError_WhenForbiddenUser() {
        client.post()
                .uri(PATH_INGREDIENTS)
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToUpdate.getId())
                .bodyValue(IngredientMapper.INSTANCE.toIngredientDTO(ingredientToUpdate.withQuantity(4)))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid ingredient")
    void update_ReturnsError_WhenInvalidIngredient() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToRead.getId())
                .bodyValue(invalidIngredientDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToRead.getId())
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        client.delete()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToDelete.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails
    @DisplayName("delete | Returns 403 error when forbidden user")
    void delete_ReturnsError_WhenForbiddenUser() {
        client.delete()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToRead.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
                .uri(PATH_INGREDIENTS.concat("/{id}"), 123)
                .exchange()
                .expectStatus().isNotFound();
    }

}
