package api.integration;

import api.mappers.UserMapper;
import api.models.entities.User;
import api.models.security.RegisterRequest;
import api.services.impl.UserService;
import api.util.UserCreator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static api.integration.constraint.IntegrationConstraints.*;

//FIXME: Corrigir essa classe de teste de integração
//@IntegrationTest
//@DisplayName("User Controller Integration Test")
class UserControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

    private static final RegisterRequest user = UserCreator.registerRequest();
    private static final User invalidUser = UserCreator.invalidUser();
    private static final User userToRead = UserCreator.userToRead();
    private static final User userToUpdate = UserCreator.userToUpdate();
    private static final User userToDelete = UserCreator.userToDelete();

    @BeforeAll
    static void beforeAll(@Autowired UserService userService) {
        Flux.just(userToRead, userToUpdate, userToDelete)
                .flatMapSequential(user -> userService.save(user)
                        .doOnNext(p -> user.setId(p.getId())))
                .blockLast(Duration.ofSeconds(5));
    }

    @AfterAll
    static void afterAll(@Autowired UserService userService) {
        userService.deleteAll().block(Duration.ofSeconds(5));
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("findById | Returns a user when successful")
    void findById() {
        client.get()
                .uri(PATH_USERS.concat("/{id}"), userToRead.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class);
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("findById | Returns 404 error when not found")
    void findById_ReturnsError_WhenNotFound() {
        client.get()
                .uri(PATH_USERS.concat("/{id}"), 123)
                .exchange()
                .expectStatus().isNotFound();
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("findByName | Returns a user when successful")
    void findByName() {
        client.get()
                .uri(builder -> builder
                        .path(PATH_USERS)
                        .queryParam("username", userToRead.getUsername())
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class);
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("findByName | Returns 404 error when not found")
    void findByName_ReturnsError_WhenNotFound() {
        client.get()
                .uri(builder -> builder.path(PATH_USERS).queryParam("username", "randomName").build())
                .exchange()
                .expectStatus().isNotFound();
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("listAll | Returns all users when successful")
    void listAll() {
        client.get()
                .uri(builder -> builder.path(PATH_USERS.concat("/all")).build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class);
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns a user when successful")
    void save() {
        client.post()
                .uri(PATH_USERS)
                .bodyValue(user)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class);
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("save | Returns 400 error when invalid user")
    void save_ReturnsError_WhenInvalidUser() {
        client.post()
                .uri(PATH_USERS)
                .bodyValue(invalidUser)
                .exchange()
                .expectStatus().isBadRequest();
    }

//    @Test
    @WithUserDetails(REGULAR_USER)
    @DisplayName("save | Returns 403 error when forbidden user")
    void save_ReturnsError_WhenForbiddenUser() {
        client.post()
                .uri(PATH_USERS)
                .bodyValue(userToRead)
                .exchange()
                .expectStatus().isForbidden();
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        client.put()
                .uri(PATH_USERS.concat("/{id}"), userToUpdate.getId())
                .bodyValue(UserMapper.INSTANCE.toRegisterRequest(userToUpdate.withUsername("New_Name")))
                .exchange()
                .expectStatus().isNoContent();
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("update | Returns 400 error when invalid user")
    void update_ReturnsError_WhenInvalidUser() {
        client.put()
                .uri(PATH_USERS.concat("/{id}"), userToUpdate.getId())
                .bodyValue(invalidUser)
                .exchange()
                .expectStatus().isBadRequest();
    }

//    @Test
    @WithUserDetails(REGULAR_USER)
    @DisplayName("update | Returns 403 error when forbidden user")
    void update_ReturnsError_WhenForbiddenUser() {
        client.put()
                .uri(PATH_USERS.concat("/{id}"), userToRead.getId())
                .bodyValue(userToRead)
                .exchange()
                .expectStatus().isForbidden();
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        System.out.println(userToDelete.getId());
        client.delete()
                .uri(PATH_USERS.concat("/{id}"), userToDelete.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

//    @Test
    @WithUserDetails(REGULAR_USER)
    @DisplayName("delete | Returns 403 error when forbidden user")
    void delete_ReturnsError_WhenForbiddenUser() {
        client.delete()
                .uri(PATH_USERS.concat("/{id}"), userToDelete.getId())
                .exchange()
                .expectStatus().isForbidden();
    }

//    @Test
    @WithUserDetails(ADMIN_USER)
    @DisplayName("delete | Returns 404 error when not found")
    void delete_ReturnsError_WhenNotFound() {
        client.delete()
                .uri(PATH_USERS.concat("/{id}"), 123)
                .exchange()
                .expectStatus().isNotFound();
    }


}
