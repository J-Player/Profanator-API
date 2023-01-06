package api.integration;

import api.configs.BlockHoundTest;
import api.domains.Item;
import api.domains.Proficiency;
import api.domains.dtos.ItemDTO;
import api.integration.annotation.IntegrationTest;
import api.mappers.ItemMapper;
import api.services.ProficiencyService;
import api.util.ItemCreator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;

import java.util.UUID;

import static api.integration.constraint.IntegrationConstraint.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraint.PATH_ITEMS;

@IntegrationTest
@DisplayName("Item Controller Integration Test")
class ItemControllerIT {

    @Autowired
    private WebTestClient client;

    private static Item item;
    private static final ItemDTO itemDTO = ItemCreator.itemDTO();
    private static final ItemDTO invalidItemDTO = ItemCreator.invalidItemDTO();

    @BeforeAll
    static void beforeAll(@Autowired ProficiencyService proficiencyService) {
        BlockHound.install();
        proficiencyService.save(Proficiency.builder()
                        .name(itemDTO.getProficiency())
                        .build())
                .map(Proficiency::getName)
                .block();
    }

    @AfterAll
    static void AfterAll(@Autowired ProficiencyService proficiencyService) {
        proficiencyService.findByName(item.getProficiency())
                .map(Proficiency::getId)
                .flatMap(proficiencyService::delete)
                .block();
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
    @DisplayName("save | Returns a item when successful")
    void save() {
        item = client.post()
                .uri(PATH_ITEMS)
                .bodyValue(itemDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Item.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    @Order(2)
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
    @Order(3)
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
    @Order(4)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), item.getId())
                .bodyValue(ItemMapper.INSTANCE.toItemDTO(item
                        .withName("New_Name")
                        .withQtByProduction(3)))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid item")
    void update_ReturnsError_WhenInvalidItem() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), item.getId())
                .bodyValue(invalidItemDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(6)
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_ITEMS.concat("/{id}"), item.getId())
                .bodyValue(itemDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @Order(7)
    @WithUserDetails
    @DisplayName("findById | Returns a item when successful")
    void findById() {
        item = client.get()
                .uri(PATH_ITEMS.concat("/{id}"), item.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    @Order(8)
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
                .uri(PATH_ITEMS.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(9)
    @WithUserDetails
    @DisplayName("findByName | Returns a item when successful")
    void findByName() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS).queryParam("name", item.getName()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .isEqualTo(item);
    }

    @Test
    @Order(10)
    @WithUserDetails
    @DisplayName("findByName | Returns 404 error when not found")
    void findByName_ReturnsError_WhenNotFound() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS).queryParam("name", "randomName").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(11)
    @WithUserDetails
    @DisplayName("listAll | Returns all items when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .contains(item);
    }

    @Test
    @Order(12)
    @WithUserDetails
    @DisplayName("listAllByProficiency | Returns all items of a proficiency when successful")
    void listAllByProficiency() {
        client.get()
                .uri(builder -> builder.path(PATH_ITEMS.concat("/all"))
                        .queryParam("proficiency", item.getProficiency()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .contains(item);
    }

    @Test
    @Order(13)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        client.delete()
                .uri(PATH_ITEMS.concat("/{id}"), item.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(14)
    @WithUserDetails
    @DisplayName("delete | Returns 403 error when forbidden user")
    void delete_ReturnsError_WhenForbiddenUser() {
        client.delete()
                .uri(PATH_ITEMS.concat("/{id}"), item.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @Order(15)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
                .uri(PATH_ITEMS.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

}
