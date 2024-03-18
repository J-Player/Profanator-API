package api.controllers;

import api.controllers.impl.UserController;
import api.models.entities.User;
import api.models.security.RegisterRequest;
import api.services.impl.UserService;
import api.util.UserCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("User Controller Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private final User user = UserCreator.user();
    private final RegisterRequest registerRequest = UserCreator.registerRequest();

    @BeforeEach
    void setUp() {
        BDDMockito.when(userService.findById(anyInt()))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.findByName(anyString()))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.findAll(Pageable.unpaged()))
                .thenReturn(Mono.just(new PageImpl<>(List.of(user))));
        BDDMockito.when(userService.save(any(User.class)))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.update(any(User.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(userService.delete(anyInt()))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findByName | Returns a user when successful")
    void findByName() {
        StepVerifier.create(userController.findByName(""))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a user when successful")
    void findById() {
        StepVerifier.create(userController.findById(1))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAll | Returns all users when successful")
    void listAll() {
        StepVerifier.create(userController.listAll(Pageable.unpaged()))
                .expectSubscription()
                .expectNext(new PageImpl<>(List.of(user)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Returns a user when successful")
    void save() {
        BDDMockito.when(userService.findByName(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userController.save(registerRequest))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        StepVerifier.create(userController.update(1, registerRequest))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(userController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

}
