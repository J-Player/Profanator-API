package api.integration;

import api.configs.BlockHoundTest;
import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
import api.integration.annotation.IntegrationTest;
import api.mappers.ProficiencyMapper;
import api.util.ProficiencyCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;

import java.util.UUID;

import static api.integration.constraint.IntegrationConstraint.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraint.PATH_PROFICIENCIES;

@IntegrationTest
@DisplayName("Proficiency Controller Integration Test")
class ProficiencyControllerIT {

    @Autowired
    private WebTestClient client;

    private static Proficiency proficiency;
    private static final ProficiencyDTO proficiencyDTO = ProficiencyCreator.proficiencyDTO();
    private static final ProficiencyDTO invalidProficiencyDTO = ProficiencyCreator.invalidProficiencyDTO();

    @BeforeAll
    static void beforeAll() {
        BlockHound.install();
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
    @DisplayName("save | Returns a proficiency when successful")
    void save() {
        proficiency = client.post()
                .uri(PATH_PROFICIENCIES)
                .bodyValue(proficiencyDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Proficiency.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    @Order(2)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns 400 error when invalid proficiency")
    void save_ReturnsError_WhenInvalidProficiency() {
        client.post()
                .uri(PATH_PROFICIENCIES)
                .bodyValue(invalidProficiencyDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    @WithUserDetails
    @DisplayName("save | Returns 403 error when forbidden user")
    void save_ReturnsError_WhenForbiddenUser() {
        client.post()
                .uri(PATH_PROFICIENCIES)
                .bodyValue(proficiencyDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @Order(4)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiency.getId())
                .bodyValue(ProficiencyMapper.INSTANCE.toProficiencyDTO(proficiency.withName("New_Name")))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(5)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid proficiency")
    void update_ReturnsError_WhenInvalidProficiency() {
        client.put()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiency.getId())
                .bodyValue(invalidProficiencyDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(6)
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiency.getId())
                .bodyValue(proficiencyDTO)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @Order(7)
    @WithUserDetails
    @DisplayName("findById | Returns a proficiency when successful")
    void findById() {
        proficiency = client.get()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiency.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Proficiency.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    @Order(8)
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(9)
    @WithUserDetails
    @DisplayName("findByName | Returns a proficiency when successful")
    void findByName() {
        client.get()
                .uri(builder -> builder.path(PATH_PROFICIENCIES).queryParam("name", proficiency.getName()).build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Proficiency.class)
                .isEqualTo(proficiency);

    }

    @Test
    @Order(10)
    @WithUserDetails
    @DisplayName("findByName | Returns 404 error when not found")
    void findByName_ReturnsError_WhenNotFound() {
        client.get()
                .uri(builder -> builder.path(PATH_PROFICIENCIES).queryParam("name", "randomName").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(11)
    @WithUserDetails
    @DisplayName("listAll | Returns all proficiencies when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH_PROFICIENCIES.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Proficiency.class)
                .contains(proficiency);
    }

    @Test
    @Order(12)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        client.delete()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiency.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(13)
    @WithUserDetails
    @DisplayName("delete | Returns 403 error when forbidden user")
    void delete_ReturnsError_WhenForbiddenUser() {
        client.delete()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiency.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @Order(14)
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), UUID.randomUUID())
                .exchange()
                .expectStatus().isNotFound();
    }

}
