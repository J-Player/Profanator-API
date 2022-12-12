package api.integration;

import api.domains.Item;
import api.domains.dtos.ItemDTO;
import api.repositories.ItemRepository;
import api.services.ProficiencyService;
import api.util.ItemCreator;
import api.util.ProficiencyCreator;
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
@DisplayName("Item Controller Integration")
class ItemControllerIT {

    private static final String ADMIN_USER = "admin";
    private static final String PATH = "/items";

    @Autowired
    private WebTestClient client;

    @MockBean
    private ProficiencyService proficiencyService;

    @MockBean
    private ItemRepository itemRepository;

    private final Item item = ItemCreator.item();
    private final ItemDTO itemDTO = ItemCreator.itemDTO();
    private final ItemDTO invalidItemDTO = ItemCreator.invalidItemDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(itemRepository.findById(any(UUID.class))).thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findByNameIgnoreCase(anyString())).thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.findAllByProficiencyIgnoreCase(anyString(), any(Sort.class))).thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.findAll(any(Sort.class))).thenReturn(Flux.just(item));
        BDDMockito.when(itemRepository.save(any(Item.class))).thenReturn(Mono.just(item));
        BDDMockito.when(itemRepository.delete(any(Item.class))).thenReturn(Mono.empty());
        BDDMockito.when(proficiencyService.findByName(anyString()))
                .thenReturn(Mono.just(ProficiencyCreator.proficiency()));
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a item when successful")
    void findById() {
        client.get()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .isEqualTo(item);
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        BDDMockito.when(itemRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.get()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns a item when successful")
    void findByName() {
        client.get()
                .uri(builder -> builder.path(PATH).queryParam("name", item.getName()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .isEqualTo(item);

    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns 404 error when not found")
    void findByName_ReturnsError_WhenNotFound() {
        BDDMockito.when(itemRepository.findByNameIgnoreCase(anyString())).thenReturn(Mono.empty());
        client.get()
                .uri(builder -> builder.path(PATH).queryParam("name", "randomName").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("listAll | Returns all items when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(1)
                .contains(item);
    }

    @Test
    @WithUserDetails
    @DisplayName("listAllByProficiency | Returns all items of a proficiency when successful")
    void listAllByProficiency() {
        client.get()
                .uri(builder -> builder.path(PATH.concat("/all"))
                        .queryParam("proficiency", item.getProficiency()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(1)
                .contains(item);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns a item when successful")
    void save() {
        client.post()
                .uri(PATH)
                .bodyValue(itemDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Item.class)
                .isEqualTo(item);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns 400 error when invalid item")
    void save_ReturnsError_WhenInvalidItem() {
        client.post()
                .uri(PATH)
                .bodyValue(invalidItemDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("save | Returns 403 error when forbidden user")
    void save_ReturnsError_WhenForbiddenUser() {
        client.post()
                .uri(PATH)
                .bodyValue(itemDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(itemDTO)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid item")
    void update_ReturnsError_WhenInvalidItem() {
        BDDMockito.when(itemRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(invalidItemDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(itemDTO)
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
        BDDMockito.when(itemRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.delete()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

}
