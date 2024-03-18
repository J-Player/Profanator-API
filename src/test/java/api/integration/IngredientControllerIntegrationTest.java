package api.integration;

<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
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
=======
import api.domains.Ingredient;
import api.domains.Item;
import api.domains.dtos.IngredientDTO;
import api.integration.annotation.IntegrationTest;
import api.utils.MapperUtil;
import api.repositories.IngredientRepository;
import api.repositories.ItemRepository;
import api.utils.IngredientCreator;
import api.utils.ItemCreator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
import static api.integration.constraint.IntegrationConstraints.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraints.PATH_INGREDIENTS;
import static api.util.ItemCreator.*;
import static api.util.ProficiencyCreator.proficiency;
=======
import static api.integration.constraint.IntegrationConstraint.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraint.PATH_INGREDIENTS;
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java

@IntegrationTest
@DisplayName("Ingredient Controller Integration Test")
class IngredientControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
    private static final IngredientDTO ingredientDTO = IngredientCreator.ingredientDTO();
=======
    private static final Logger log = LoggerFactory.getLogger(ItemControllerIT.class);

    private static final Ingredient ingredient = IngredientCreator.ingredient();
    private static final IngredientDTO ingredientToSave = IngredientCreator.ingredientToSave();
    private static final Ingredient ingredientToUpdate = IngredientCreator.ingredientToUpdate();
    private static final Ingredient ingredientToDelete = IngredientCreator.ingredientToDelete();
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
    private static final IngredientDTO invalidIngredientDTO = IngredientCreator.invalidIngredientDTO();
    private static final Ingredient ingredientToRead = IngredientCreator.ingredientToRead();
    private static final Ingredient ingredientToUpdate = IngredientCreator.ingredientToUpdate();
    private static final Ingredient ingredientToDelete = IngredientCreator.ingredientToDelete();

    @BeforeAll
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
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
=======
    static void beforeAll(@Autowired ItemRepository itemRepository) {
        Item item1 = ItemCreator.item().withProficiency(null).withName(ingredient.getProduct());
        Item item2 = ItemCreator.item().withProficiency(null).withName(ingredient.getName());
        Item item3 = ItemCreator.item().withProficiency(null).withName(ingredientToSave.getName());
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
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a ingredient when successful")
    void findById() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToRead.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ingredient.class);
=======
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredient.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Ingredient.class)
                .value(Ingredient::getId, Matchers.equalTo(ingredient.getId()))
                .value(Ingredient::getProduct, Matchers.equalTo(ingredient.getProduct()))
                .value(Ingredient::getName, Matchers.equalTo(ingredient.getName()))
                .value(Ingredient::getQuantity, Matchers.equalTo(ingredient.getQuantity()));
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                .uri(PATH_INGREDIENTS.concat("/{id}"), 123)
=======
                .uri(PATH_INGREDIENTS.concat("/{id}"), 123L)
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
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
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                        .queryParam("item", ingredientToRead.getProduct()).build())
=======
                        .queryParam("item", ingredient.getProduct()).build())
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
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
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                .expectBody(Ingredient.class);
=======
                .expectBody(Ingredient.class)
                .value(Ingredient::getProduct, Matchers.equalTo(ingredientToSave.getProduct()))
                .value(Ingredient::getName, Matchers.equalTo(ingredientToSave.getName()))
                .value(Ingredient::getQuantity, Matchers.equalTo(ingredientToSave.getQuantity()));
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
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
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                .bodyValue(IngredientMapper.INSTANCE.toIngredientDTO(ingredientToUpdate.withQuantity(4)))
=======
                .bodyValue(MapperUtil.MAPPER.map(ingredientToUpdate.withQuantity(7), IngredientDTO.class))
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid ingredient")
    void update_ReturnsError_WhenInvalidIngredient() {
        client.put()
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToRead.getId())
=======
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToUpdate.getId())
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
                .bodyValue(invalidIngredientDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToRead.getId())
                .bodyValue(ingredientDTO)
=======
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToUpdate.getId())
                .bodyValue(MapperUtil.MAPPER.map(ingredientToUpdate.withQuantity(7), IngredientDTO.class))
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
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
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToRead.getId())
=======
                .uri(PATH_INGREDIENTS.concat("/{id}"), ingredientToDelete.getId())
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
<<<<<<< HEAD:src/test/java/api/integration/IngredientControllerIntegrationTest.java
                .uri(PATH_INGREDIENTS.concat("/{id}"), 123)
=======
                .uri(PATH_INGREDIENTS.concat("/{id}"), 123L)
>>>>>>> main:src/test/java/api/integration/IngredientControllerIT.java
                .exchange()
                .expectStatus().isNotFound();
    }

}
