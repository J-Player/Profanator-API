package api.integration;

import api.configs.BlockHoundTest;
import api.domains.Ingredient;
import api.domains.Proficiency;
import api.domains.dtos.IngredientDTO;
import api.integration.annotation.IntegrationTest;
import api.mappers.IngredientMapper;
import api.services.ItemService;
import api.services.ProficiencyService;
import api.util.IngredientCreator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static api.integration.constraint.IntegrationConstraint.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraint.PATH_INGREDIENTS;
import static api.util.ItemCreator.item;
import static api.util.ProficiencyCreator.proficiency;

@IntegrationTest
@DisplayName("Ingredient Controller Integration Test")
class IngredientControllerIT {

    @Autowired
    private WebTestClient client;

    private static Ingredient ingredient;
    private static final IngredientDTO ingredientDTO = IngredientCreator.ingredientDTO();
    private static final IngredientDTO invalidIngredientDTO = IngredientCreator.invalidIngredientDTO();

    @BeforeAll
    static void beforeAll(@Autowired ProficiencyService proficiencyService, @Autowired ItemService itemService) {
        BlockHound.install();
        proficiencyService.save(proficiency().withId(null))
                .map(Proficiency::getName)
                .flatMapMany(proficiency -> Flux.just(
                        item()
                                .withId(null)
                                .withProficiency(proficiency)
                                .withName(ingredientDTO.getProduct()),
                        item()
                                .withId(null)
                                .withProficiency(proficiency)
                                .withName(ingredientDTO.getName())))
                .flatMap(itemService::save)
                .blockLast();
    }

    @AfterAll
    static void afterAll(@Autowired ProficiencyService proficiencyService, @Autowired ItemService itemService) {
        itemService.findByName(ingredient.getProduct())
                .mergeWith(itemService.findByName(ingredient.getName()))
                .flatMap(item -> itemService.delete(item.getId()))
                .concatWith(proficiencyService.findByName(proficiency().getName())
                        .map(Proficiency::getId)
                        .flatMap(proficiencyService::delete))
                .blockLast();
    }

    @Test
    @Order(0)
    @DisplayName("[BlockHound] Check if BlockHound is working")
    void blockHoundWorks() {
        BlockHoundTest.test();
    }

    @Test
    @Order(1)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns a ingredient when successful")
    void save() {
        ingredient = client.post()
                .uri(PATH_INGREDIENTS)
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Ingredient.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    @Order(2)
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
    @Order(3)
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
    @Order(4)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredient.getId())
                .bodyValue(IngredientMapper.INSTANCE.toIngredientDTO(ingredient.withQuantity(4)))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid ingredient")
    void update_ReturnsError_WhenInvalidIngredient() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredient.getId())
                .bodyValue(invalidIngredientDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(6)
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredient.getId())
                .bodyValue(ingredientDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @Order(7)
    @WithUserDetails
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
        ingredient = client.get()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredient.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ingredient.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    @Order(8)
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
                .uri(PATH_INGREDIENTS.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(9)
    @WithUserDetails
    @DisplayName("listAll | Returns all ingredients when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH_INGREDIENTS.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Ingredient.class)
                .contains(ingredient);
    }

    @Test
    @Order(10)
    @WithUserDetails
    @DisplayName("listAllByItem | Returns all ingredients of a item when successful")
    void listAllByItem() {
        client.get()
                .uri(builder -> builder.path(PATH_INGREDIENTS.concat("/all"))
                        .queryParam("item", ingredient.getProduct()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Ingredient.class)
                .hasSize(1)
                .contains(ingredient);
    }

    @Test
    @Order(11)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        client.delete()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredient.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(12)
    @WithUserDetails
    @DisplayName("delete | Returns 403 error when forbidden user")
    void delete_ReturnsError_WhenForbiddenUser() {
        client.delete()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredient.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @Order(13)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
                .uri(PATH_INGREDIENTS.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

}
