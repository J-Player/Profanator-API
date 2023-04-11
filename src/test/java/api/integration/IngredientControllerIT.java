package api.integration;

import api.domains.Ingredient;
import api.domains.Item;
import api.domains.dtos.IngredientDTO;
import api.integration.annotation.IntegrationTest;
import api.mappers.IngredientMapper;
import api.repositories.IngredientRepository;
import api.repositories.ItemRepository;
import api.utils.IngredientCreator;
import api.utils.ItemCreator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static api.integration.constraint.IntegrationConstraint.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraint.PATH_INGREDIENTS;

@IntegrationTest
@DisplayName("Ingredient Controller Integration Test")
class IngredientControllerIT {

    @Autowired
    private WebTestClient client;

    private static final Logger log = LoggerFactory.getLogger(ItemControllerIT.class);

    private static final Ingredient ingredient = IngredientCreator.ingredient();
    private static final IngredientDTO ingredientToSave = IngredientCreator.ingredientToSave();
    private static final Ingredient ingredientToUpdate = IngredientCreator.ingredientToUpdate();
    private static final Ingredient ingredientToDelete = IngredientCreator.ingredientToDelete();
    private static final IngredientDTO invalidIngredientDTO = IngredientCreator.invalidIngredientDTO();

    @BeforeAll
    static void beforeAll(@Autowired ItemRepository itemRepository) {
        Item item1 = ItemCreator.item().withProficiency(null).withName(ingredient.getProduct());
        Item item2 = ItemCreator.item().withProficiency(null).withName(ingredient.getName());
        Item item3 = ItemCreator.item().withProficiency(null).withName(ingredientToSave.name());
        Item item4 = ItemCreator.item().withProficiency(null).withName(ingredientToUpdate.getName());
        Item item5 = ItemCreator.item().withProficiency(null).withName(ingredientToDelete.getName());
        Flux.just(item1, item2, item3, item4, item5)
                .flatMapSequential(i -> itemRepository.findByNameIgnoreCase(i.getName())
                        .switchIfEmpty(itemRepository.save(i.withId(null))
                                .doOnNext(item -> log.info("Item salvo com sucesso! {}", item))))
                .blockLast(Duration.ofSeconds(10));
    }

    @AfterAll
    static void afterAll(@Autowired ItemRepository itemRepository,
                         @Autowired IngredientRepository ingredientRepository) {
        ingredientRepository.deleteAll().then(itemRepository.deleteAll());
    }

    @BeforeEach
    void setUp(@Autowired IngredientRepository repository) {
        Flux.just(ingredient, ingredientToUpdate, ingredientToDelete)
                .flatMapSequential(ingredient -> repository
                        .findByProductAndNameAllIgnoreCase(ingredient.getProduct(), ingredient.getName())
                        .switchIfEmpty(repository.save(ingredient.withId(null))
                                .doOnNext(ing -> ingredient.setId(ing.getId()))
                                .doOnSuccess(ing -> log.info("Ingredient salvo com sucesso! {}", ingredient))))
                .blockLast(Duration.ofSeconds(10));
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
        client.get()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredient.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ingredient.class)
                .value(Ingredient::getId, Matchers.equalTo(ingredient.getId()))
                .value(Ingredient::getProduct, Matchers.equalTo(ingredient.getProduct()))
                .value(Ingredient::getName, Matchers.equalTo(ingredient.getName()))
                .value(Ingredient::getQuantity, Matchers.equalTo(ingredient.getQuantity()));
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
                .uri(PATH_INGREDIENTS.concat("/{id}"), 123L)
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
                        .queryParam("item", ingredient.getProduct()).build())
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
                .bodyValue(ingredientToSave)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Ingredient.class)
                .value(Ingredient::getProduct, Matchers.equalTo(ingredientToSave.product()))
                .value(Ingredient::getName, Matchers.equalTo(ingredientToSave.name()))
                .value(Ingredient::getQuantity, Matchers.equalTo(ingredientToSave.quantity()));
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
                .bodyValue(ingredientToSave)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToUpdate.getId())
                .bodyValue(IngredientMapper.INSTANCE.toIngredientDTO(ingredientToUpdate.withQuantity(7)))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid ingredient")
    void update_ReturnsError_WhenInvalidIngredient() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToUpdate.getId())
                .bodyValue(invalidIngredientDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToUpdate.getId())
                .bodyValue(IngredientMapper.INSTANCE.toIngredientDTO(ingredientToUpdate.withQuantity(7)))
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
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToDelete.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
                .uri(PATH_INGREDIENTS.concat("/{id}"), 123L)
                .exchange()
                .expectStatus().isNotFound();
    }

}
