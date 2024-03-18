package api.controllers;

<<<<<<< HEAD
import api.controllers.impl.UserController;
import api.models.entities.User;
import api.models.security.RegisterRequest;
=======
import api.domains.User;
import api.domains.dtos.UserDTO;
>>>>>>> main
import api.services.impl.UserService;
import api.utils.UserCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
<<<<<<< HEAD
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

=======
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

>>>>>>> main
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
<<<<<<< HEAD
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
=======
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
>>>>>>> main
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.update(any(UserDTO.class), anyLong()))
                .thenReturn(Mono.empty());
<<<<<<< HEAD
        BDDMockito.when(userService.delete(anyInt()))
=======
        BDDMockito.when(userService.delete(anyLong()))
>>>>>>> main
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
<<<<<<< HEAD
        StepVerifier.create(userController.findById(1))
=======
        StepVerifier.create(userController.findById(1L))
>>>>>>> main
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
<<<<<<< HEAD
        StepVerifier.create(userController.update(1, registerRequest))
=======
        StepVerifier.create(userController.update(userDTO, 1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
<<<<<<< HEAD
        StepVerifier.create(userController.delete(1))
=======
        StepVerifier.create(userController.delete(1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }

}
