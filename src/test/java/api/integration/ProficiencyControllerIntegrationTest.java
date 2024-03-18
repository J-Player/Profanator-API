package api.integration;

<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
import api.integration.annotation.IntegrationTest;
import api.mappers.ProficiencyMapper;
import api.models.dtos.ProficiencyDTO;
import api.models.entities.Proficiency;
import api.services.impl.ProficiencyService;
import api.util.ProficiencyCreator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
=======
import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
import api.integration.annotation.IntegrationTest;
import api.utils.MapperUtil;
import api.repositories.ProficiencyRepository;
import api.utils.ProficiencyCreator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static api.integration.constraint.IntegrationConstraints.ADMIN_USER;
import static api.integration.constraint.IntegrationConstraints.PATH_PROFICIENCIES;

@IntegrationTest
@DisplayName("Proficiency Controller Integration Test")
class ProficiencyControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
    private static final ProficiencyDTO proficiencyDTO = ProficiencyCreator.proficiencyDTO();
=======
    private static final Logger log = LoggerFactory.getLogger(ProficiencyControllerIT.class);

    private static final Proficiency proficiency = ProficiencyCreator.proficiency();
    private static final ProficiencyDTO proficiencyToSave = ProficiencyCreator.proficiencyToSave();
    private static final Proficiency proficiencyToUpdate = ProficiencyCreator.proficiencyToUpdate();
    private static final Proficiency proficiencyToDelete = ProficiencyCreator.proficiencyToDelete();
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
    private static final ProficiencyDTO invalidProficiencyDTO = ProficiencyCreator.invalidProficiencyDTO();
    private static final Proficiency proficiencyToRead = ProficiencyCreator.ProficiencyToRead();
    private static final Proficiency proficiencyToUpdate = ProficiencyCreator.proficiencyToUpdate();
    private static final Proficiency proficiencyToDelete = ProficiencyCreator.proficiencyToDelete();

<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
    @BeforeAll
    static void beforeAll(@Autowired ProficiencyService proficiencyService) {
        Flux.just(proficiencyToRead, proficiencyToUpdate, proficiencyToDelete)
                .flatMapSequential(proficiency -> proficiencyService.save(proficiency)
                        .doOnNext(p -> proficiency.setId(p.getId())))
                .blockLast(Duration.ofSeconds(5));
    }

    @AfterAll
    static void afterAll(@Autowired ProficiencyService proficiencyService) {
        proficiencyService.deleteAll().block(Duration.ofSeconds(5));
=======
    @BeforeEach
    void setUp(@Autowired ProficiencyRepository repository) {
        Flux.just(proficiency, proficiencyToUpdate, proficiencyToDelete)
                .flatMapSequential(proficiency -> repository.findByNameIgnoreCase(proficiency.getName())
                        .switchIfEmpty(repository.save(proficiency.withId(null))
                                .doOnNext(p -> proficiency.setId(p.getId()))
                                .doOnSuccess(p -> log.info("Proficiência salva com sucesso! {}", p))))
                .blockLast(Duration.ofSeconds(10));
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns a proficiency when successful")
    void findById() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiencyToRead.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Proficiency.class);
=======
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiency.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Proficiency.class)
                .value(Proficiency::getId, Matchers.equalTo(proficiency.getId()))
                .value(Proficiency::getName, Matchers.equalTo(proficiency.getName()));
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
    }

    @Test
    @WithUserDetails
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
                .uri(PATH_PROFICIENCIES.concat("/{id}"), 123)
=======
                .uri(PATH_PROFICIENCIES.concat("/{id}"), 123L)
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns a proficiency when successful")
    void findByName() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
                .uri(builder -> builder
                        .path(PATH_PROFICIENCIES)
                        .queryParam("name", proficiencyToRead.getName())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Proficiency.class);
=======
                .uri(builder -> builder.path(PATH_PROFICIENCIES)
                        .queryParam("name", proficiency.getName())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Proficiency.class)
                .value(Proficiency::getId, Matchers.equalTo(proficiency.getId()))
                .value(Proficiency::getName, Matchers.equalTo(proficiency.getName()));

>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
    }

    @Test
    @WithUserDetails
    @DisplayName("findByName | Returns 404 error when not found")
    void findByName_ReturnsError_WhenNotFound() {
        client.get()
<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
                .uri(builder -> builder.path(PATH_PROFICIENCIES).queryParam("name", "randomName").build())
=======
                .uri(builder -> builder.path(PATH_PROFICIENCIES)
                        .queryParam("name", "randomName")
                        .build())
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithUserDetails
    @DisplayName("listAll | Returns all proficiencies when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH_PROFICIENCIES.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Proficiency.class);
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns a proficiency when successful")
    void save() {
        client.post()
                .uri(PATH_PROFICIENCIES)
                .bodyValue(proficiencyToSave)
                .exchange()
                .expectStatus().isCreated()
<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
                .expectBody(Proficiency.class);
=======
                .expectBody(Proficiency.class)
                .value(Proficiency::getName, Matchers.equalTo(proficiencyToSave.getName()));
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
    }

    @Test
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
    @WithUserDetails
    @DisplayName("save | Returns 403 error when forbidden user")
    void save_ReturnsError_WhenForbiddenUser() {
        client.post()
                .uri(PATH_PROFICIENCIES)
                .bodyValue(proficiencyToSave)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiencyToUpdate.getId())
<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
                .bodyValue(ProficiencyMapper.INSTANCE.toProficiencyDTO(proficiencyToUpdate.withName("New_Name")))
=======
                .bodyValue(MapperUtil.MAPPER.map(proficiencyToUpdate.withName("New_Name"), ProficiencyDTO.class))
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid proficiency")
    void update_ReturnsError_WhenInvalidProficiency() {
        client.put()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiencyToUpdate.getId())
                .bodyValue(invalidProficiencyDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithUserDetails
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiencyToUpdate.getId())
<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
                .bodyValue(proficiencyDTO)
=======
                .bodyValue(MapperUtil.MAPPER.map(proficiencyToUpdate.withName("New_Name"), ProficiencyDTO.class))
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        client.delete()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiencyToDelete.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @WithUserDetails
    @DisplayName("delete | Returns 403 error when forbidden user")
    void delete_ReturnsError_WhenForbiddenUser() {
        client.delete()
                .uri(PATH_PROFICIENCIES.concat("/{id}"), proficiencyToDelete.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
<<<<<<< HEAD:src/test/java/api/integration/ProficiencyControllerIntegrationTest.java
                .uri(PATH_PROFICIENCIES.concat("/{id}"), 123)
=======
                .uri(PATH_PROFICIENCIES.concat("/{id}"), 123L)
>>>>>>> main:src/test/java/api/integration/ProficiencyControllerIT.java
                .exchange()
                .expectStatus().isNotFound();
    }

}
