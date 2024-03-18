package api.integration;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Objects;

import static api.integration.constraint.IntegrationConstraints.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraints.PATH_ITEMS;

@IntegrationTest
@DisplayName("Item Controller Integration Test")
class ItemControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

    private static final ItemDTO itemDTO = ItemCreator.itemDTO();
    private static final ItemDTO invalidItemDTO = ItemCreator.invalidItemDTO();
    private static final Item itemToRead = ItemCreator.itemToRead();
    private static final Item itemToUpdate = ItemCreator.itemToUpdate();
    private static final Item itemToDelete = ItemCreator.itemToDelete();

    @BeforeAll
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
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a item when successful")
    void findById() {
        client.get()
                .uri(PATH_ITEMS.concat("/{id}"), itemToRead.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class);
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
                .uri(PATH_ITEMS.concat("/{id}"), 123)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns a item when successful")
    void findByName() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS).queryParam("name", itemToRead.getName()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class);
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
                        .queryParam("proficiency", itemToRead.getProficiency()).build())
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
                .bodyValue(itemDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Item.class);
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
                .bodyValue(itemDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
                .bodyValue(ItemMapper.INSTANCE.toItemDTO(itemToUpdate
                        .withName("New_Name")
                        .withQtByProduction(3)))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
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
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), itemToUpdate.getId())
                .bodyValue(itemDTO)
                .exchange()
                .expectStatus().isForbidden();
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
                .uri(PATH_ITEMS.concat("/{id}"), 123)
                .exchange()
                .expectStatus().isNotFound();
    }

}
