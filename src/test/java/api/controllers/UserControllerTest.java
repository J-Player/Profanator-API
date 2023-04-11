package api.controllers;

import api.domains.User;
import api.domains.dtos.UserDTO;
import api.services.impl.UserService;
import api.utils.UserCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("User Controller Test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private final User user = UserCreator.user();
    private final UserDTO userDTO = UserCreator.userDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(userService.findById(anyLong()))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.findByName(anyString()))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.findAll())
                .thenReturn(Flux.just(user));
        BDDMockito.when(userService.save(any(UserDTO.class)))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.update(any(UserDTO.class), anyLong()))
                .thenReturn(Mono.empty());
        BDDMockito.when(userService.delete(anyLong()))
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
        StepVerifier.create(userController.findById(1L))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAll | Returns all users when successful")
    void listAll() {
        StepVerifier.create(userController.listAll())
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Returns a user when successful")
    void save() {
        StepVerifier.create(userController.save(userDTO))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        StepVerifier.create(userController.update(userDTO, 1L))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(userController.delete(1L))
                .expectSubscription()
                .verifyComplete();
    }

}
