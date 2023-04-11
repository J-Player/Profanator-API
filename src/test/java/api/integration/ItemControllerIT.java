package api.integration;

import api.domains.Item;
import api.domains.Proficiency;
import api.domains.dtos.ItemDTO;
import api.integration.annotation.IntegrationTest;
import api.mappers.ItemMapper;
import api.repositories.ItemRepository;
import api.repositories.ProficiencyRepository;
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
import static api.integration.constraint.IntegrationConstraint.PATH_ITEMS;

@IntegrationTest
@DisplayName("Item Controller Integration Test")
class ItemControllerIT {

    @Autowired
    private WebTestClient client;

    private static final Logger log = LoggerFactory.getLogger(ItemControllerIT.class);

    private static final Item item = ItemCreator.item();
    private static final ItemDTO itemToSave = ItemCreator.itemToSave();
    private static final Item itemToUpdate = ItemCreator.itemToUpdate();
    private static final Item itemToDelete = ItemCreator.itemToDelete();
    private static final ItemDTO invalidItemDTO = ItemCreator.invalidItemDTO();

    @BeforeAll
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
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a item when successful")
    void findById() {
        client.get()
                .uri(PATH_ITEMS.concat("/{id}"), item.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .value(Item::getId, Matchers.equalTo(item.getId()))
                .value(Item::getProficiency, Matchers.equalTo(item.getProficiency()))
                .value(Item::getName, Matchers.equalTo(item.getName()))
                .value(Item::getQtByProduction, Matchers.equalTo(item.getQtByProduction()));
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
                .uri(PATH_ITEMS.concat("/{id}"), 123L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns a item when successful")
    void findByName() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS).queryParam("name", item.getName()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .value(Item::getId, Matchers.equalTo(item.getId()))
                .value(Item::getProficiency, Matchers.equalTo(item.getProficiency()))
                .value(Item::getName, Matchers.equalTo(item.getName()))
                .value(Item::getQtByProduction, Matchers.equalTo(item.getQtByProduction()));
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
                        .queryParam("proficiency", item.getProficiency()).build())
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
                .expectBody(Item.class)
                .value(Item::getProficiency, Matchers.equalTo(itemToSave.proficiency()))
                .value(Item::getName, Matchers.equalTo(itemToSave.name()))
                .value(Item::getQtByProduction, Matchers.equalTo(itemToSave.qtByProduction()));
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
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns error 500 when trying to save an item with a non-existent Proficiency.")
    void save_ReturnsError_WhenProficiencyNotFound() {
        String name = itemToSave.name();
        Integer qtByProduction = itemToSave.qtByProduction();
        ItemDTO itemWithInvalidProficiency = new ItemDTO("Random_Proficiency", name, qtByProduction);
        client.post()
                .uri(PATH_ITEMS)
                .bodyValue(itemWithInvalidProficiency)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
                .bodyValue(ItemMapper.INSTANCE.toItemDTO(itemToUpdate.withName("New_Name")))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
                .bodyValue(ItemMapper.INSTANCE.toItemDTO(itemToUpdate.withName("New_Name")))
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns error 500 when trying to update an item with a non-existent Proficiency.")
    void update_ReturnsError_WhenProficiencyNotFound() {
        Item item = ItemCreator.itemToUpdate().withProficiency("Random_Proficiency");
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
                .bodyValue(ItemMapper.INSTANCE.toItemDTO(item))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
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
                .uri(PATH_ITEMS.concat("/{id}"), 123L)
                .exchange()
                .expectStatus().isNotFound();
    }

}
