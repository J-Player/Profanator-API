package api.integration;

import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
import api.repositories.ProficiencyRepository;
import api.util.ProficiencyCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@DisplayName("Proficiency Controller Integration")
class ProficiencyControllerIT {

    private static final String ADMIN_USER = "admin";
    private static final String PATH = "/proficiencies";

    @Autowired
    private WebTestClient client;

    @MockBean
    private ProficiencyRepository proficiencyRepository;

    private final Proficiency proficiency = ProficiencyCreator.proficiency();
    private final ProficiencyDTO proficiencyDTO = ProficiencyCreator.proficiencyDTO();
    private final ProficiencyDTO invalidProficiencyDTO = ProficiencyCreator.invalidProficiencyDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(proficiencyRepository.findById(any(UUID.class))).thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.findByNameIgnoreCase(anyString())).thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.findAll(any(Sort.class))).thenReturn(Flux.just(proficiency));
        BDDMockito.when(proficiencyRepository.save(any(Proficiency.class))).thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.delete(any(Proficiency.class))).thenReturn(Mono.empty());
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a proficiency when successful")
    void findById() {
        client.get()
                .uri(PATH.concat("/{id}"), proficiency.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Proficiency.class)
                .isEqualTo(proficiency);
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        BDDMockito.when(proficiencyRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.get()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns a proficiency when successful")
    void findByName() {
        client.get()
                .uri(builder -> builder.path(PATH).queryParam("name", proficiency.getName()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Proficiency.class)
                .isEqualTo(proficiency);

    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns 404 error when not found")
    void findByName_ReturnsError_WhenNotFound() {
        BDDMockito.when(proficiencyRepository.findByNameIgnoreCase(anyString())).thenReturn(Mono.empty());
        client.get()
                .uri(builder -> builder.path(PATH).queryParam("name", "randomName").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("listAll | Returns all proficiencies when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Proficiency.class)
                .hasSize(1)
                .contains(proficiency);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns a proficiency when successful")
    void save() {
        client.post()
                .uri(PATH)
                .bodyValue(proficiencyDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Proficiency.class)
                .isEqualTo(proficiency);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns 400 error when invalid proficiency")
    void save_ReturnsError_WhenInvalidProficiency() {
        client.post()
                .uri(PATH)
                .bodyValue(invalidProficiencyDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("save | Returns 403 error when forbidden user")
    void save_ReturnsError_WhenForbiddenUser() {
        client.post()
                .uri(PATH)
                .bodyValue(proficiencyDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(proficiencyDTO)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid proficiency")
    void update_ReturnsError_WhenInvalidProficiency() {
        BDDMockito.when(proficiencyRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(invalidProficiencyDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .bodyValue(proficiencyDTO)
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
        BDDMockito.when(proficiencyRepository.findById(any(UUID.class))).thenReturn(Mono.empty());
        client.delete()
                .uri(PATH.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

}
