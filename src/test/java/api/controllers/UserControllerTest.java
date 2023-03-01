package api.controllers;

import api.configs.BlockHoundTest;
import api.domains.User;
import api.domains.dtos.UserDTO;
import api.services.impl.UserService;
import api.util.UserCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@DisplayName("User Controller Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private final User user = UserCreator.user();
    private final UserDTO userDTO = UserCreator.userDTO();

    @BeforeAll
    public static void blockHound() {
        BlockHound.install();
    }

    @Test
    @Order(-1)
    @DisplayName("[BlockHound] Check if BlockHound is working")
    void blockHoundWorks() {
        BlockHoundTest.test();
    }

    @BeforeEach
    void setUp() {
        BDDMockito.when(userService.findById(any(UUID.class)))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.findByName(anyString()))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.findAll())
                .thenReturn(Flux.just(user));
        BDDMockito.when(userService.save(any(User.class)))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userService.update(any(User.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(userService.delete(any(UUID.class)))
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
        StepVerifier.create(userController.findById(UUID.randomUUID()))
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
        StepVerifier.create(userController.update(UUID.randomUUID(), userDTO))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(userController.delete(UUID.randomUUID()))
                .expectSubscription()
                .verifyComplete();
    }

}
