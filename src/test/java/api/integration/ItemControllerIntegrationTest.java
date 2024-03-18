package api.integration;

<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
import api.integration.annotation.IntegrationTest;
import api.mappers.ItemMapper;
import api.models.dtos.ItemDTO;
import api.models.entities.Item;
import api.services.impl.ItemService;
import api.services.impl.ProficiencyService;
import api.util.ItemCreator;
import api.util.ProficiencyCreator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
=======
import api.domains.Item;
import api.domains.Proficiency;
import api.domains.dtos.ItemDTO;
import api.integration.annotation.IntegrationTest;
import api.utils.MapperUtil;
import api.repositories.ItemRepository;
import api.repositories.ProficiencyRepository;
import api.utils.ItemCreator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
import java.util.Objects;
=======
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java

import static api.integration.constraint.IntegrationConstraints.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraints.PATH_ITEMS;

@IntegrationTest
@DisplayName("Item Controller Integration Test")
class ItemControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
    private static final ItemDTO itemDTO = ItemCreator.itemDTO();
=======
    private static final Logger log = LoggerFactory.getLogger(ItemControllerIT.class);

    private static final Item item = ItemCreator.item();
    private static final ItemDTO itemToSave = ItemCreator.itemToSave();
    private static final Item itemToUpdate = ItemCreator.itemToUpdate();
    private static final Item itemToDelete = ItemCreator.itemToDelete();
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
    private static final ItemDTO invalidItemDTO = ItemCreator.invalidItemDTO();
    private static final Item itemToRead = ItemCreator.itemToRead();
    private static final Item itemToUpdate = ItemCreator.itemToUpdate();
    private static final Item itemToDelete = ItemCreator.itemToDelete();

    @BeforeAll
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
    static void beforeAll(@Autowired ProficiencyService proficiencyService, @Autowired ItemService itemService) {
        proficiencyService.save(ProficiencyCreator.proficiency())
                .filter(Objects::nonNull)
                .flatMapMany(proficiency ->
                        Flux.just(itemToRead, itemToUpdate, itemToDelete)
                                .flatMapSequential(item -> itemService.save(item)
                                        .doOnNext(i -> item.setId(i.getId()))))
                .blockLast(Duration.ofSeconds(5));
    }

    @AfterAll
    static void AfterAll(@Autowired ProficiencyService proficiencyService, @Autowired ItemService itemService) {
        itemService.deleteAll()
                .concatWith(proficiencyService.deleteAll())
                .blockLast(Duration.ofSeconds(5));
=======
    static void beforeAll(@Autowired ProficiencyRepository proficiencyRepository) {
        proficiencyRepository.save(Proficiency.builder()
                .name(item.getProficiency())
                .build()).block(Duration.ofSeconds(10));
    }

    @AfterAll
    static void AfterAll(@Autowired ProficiencyRepository proficiencyRepository, @Autowired ItemRepository itemRepository) {
        itemRepository.deleteAll().then(proficiencyRepository.deleteAll());
    }

    @BeforeEach
    void setUp(@Autowired ItemRepository repository) {
        Flux.just(item, itemToUpdate, itemToDelete)
                .flatMapSequential(item -> repository.findByNameIgnoreCase(item.getName())
                        .switchIfEmpty(repository.save(item.withId(null))
                                .doOnNext(i -> item.setId(i.getId()))
                                .doOnSuccess(i -> log.info("Item salvo com sucesso! {}", item))))
                .blockLast(Duration.ofSeconds(10));
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a item when successful")
    void findById() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
                .uri(PATH_ITEMS.concat("/{id}"), itemToRead.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class);
=======
                .uri(PATH_ITEMS.concat("/{id}"), item.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .value(Item::getId, Matchers.equalTo(item.getId()))
                .value(Item::getProficiency, Matchers.equalTo(item.getProficiency()))
                .value(Item::getName, Matchers.equalTo(item.getName()))
                .value(Item::getQtByProduction, Matchers.equalTo(item.getQtByProduction()));
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
                .uri(PATH_ITEMS.concat("/{id}"), 123)
=======
                .uri(PATH_ITEMS.concat("/{id}"), 123L)
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns a item when successful")
    void findByName() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
                .uri(builder -> builder.path(PATH_ITEMS).queryParam("name", itemToRead.getName()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class);
=======
                .uri(builder -> builder.path(PATH_ITEMS).queryParam("name", item.getName()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .value(Item::getId, Matchers.equalTo(item.getId()))
                .value(Item::getProficiency, Matchers.equalTo(item.getProficiency()))
                .value(Item::getName, Matchers.equalTo(item.getName()))
                .value(Item::getQtByProduction, Matchers.equalTo(item.getQtByProduction()));
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns 404 error when not found")
    void findByName_ReturnsError_WhenNotFound() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS).queryParam("name", "randomName").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("listAll | Returns all items when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class);
    }

    @Test
    @WithUserDetails
    @DisplayName("listAllByProficiency | Returns all items of a proficiency when successful")
    void listAllByProficiency() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS.concat("/all"))
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
                        .queryParam("proficiency", itemToRead.getProficiency()).build())
=======
                        .queryParam("proficiency", item.getProficiency()).build())
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns a item when successful")
    void save() {
        client.post()
                .uri(PATH_ITEMS)
                .bodyValue(itemToSave)
                .exchange()
                .expectStatus().isCreated()
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
                .expectBody(Item.class);
=======
                .expectBody(Item.class)
                .value(Item::getProficiency, Matchers.equalTo(itemToSave.getProficiency()))
                .value(Item::getName, Matchers.equalTo(itemToSave.getName()))
                .value(Item::getQtByProduction, Matchers.equalTo(itemToSave.getQtByProduction()));
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns 400 error when invalid item")
    void save_ReturnsError_WhenInvalidItem() {
        client.post()
                .uri(PATH_ITEMS)
                .bodyValue(invalidItemDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("save | Returns 403 error when forbidden user")
    void save_ReturnsError_WhenForbiddenUser() {
        client.post()
                .uri(PATH_ITEMS)
                .bodyValue(itemToSave)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
=======
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns error 500 when trying to save an item with a non-existent Proficiency.")
    void save_ReturnsError_WhenProficiencyNotFound() {
        String name = itemToSave.getName();
        Integer qtByProduction = itemToSave.getQtByProduction();
        ItemDTO itemWithInvalidProficiency = new ItemDTO("Random_Proficiency", name, qtByProduction);
        client.post()
                .uri(PATH_ITEMS)
                .bodyValue(itemWithInvalidProficiency)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
                .bodyValue(ItemMapper.INSTANCE.toItemDTO(itemToUpdate
                        .withName("New_Name")
                        .withQtByProduction(3)))
=======
                .bodyValue(MapperUtil.MAPPER.map(itemToUpdate.withName("New_Name"), ItemDTO.class))
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid item")
    void update_ReturnsError_WhenInvalidItem() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
                .bodyValue(invalidItemDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
=======
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
                .bodyValue(itemDTO)
=======
                .bodyValue(MapperUtil.MAPPER.map(itemToUpdate.withName("New_Name"), ItemDTO.class))
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
=======
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns error 500 when trying to update an item with a non-existent Proficiency.")
    void update_ReturnsError_WhenProficiencyNotFound() {
        Item item = ItemCreator.itemToUpdate().withProficiency("Random_Proficiency");
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
                .bodyValue(MapperUtil.MAPPER.map(item, ItemDTO.class))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        client.delete()
                .uri(PATH_ITEMS.concat("/{id}"), itemToDelete.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails
    @DisplayName("delete | Returns 403 error when forbidden user")
    void delete_ReturnsError_WhenForbiddenUser() {
        client.delete()
                .uri(PATH_ITEMS.concat("/{id}"), itemToDelete.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
<<<<<<< HEAD:src/test/java/api/integration/ItemControllerIntegrationTest.java
                .uri(PATH_ITEMS.concat("/{id}"), 123)
=======
                .uri(PATH_ITEMS.concat("/{id}"), 123L)
>>>>>>> main:src/test/java/api/integration/ItemControllerIT.java
                .exchange()
                .expectStatus().isNotFound();
    }

}
