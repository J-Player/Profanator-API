package api.controllers;

import api.domains.User;
import api.domains.dtos.UserDTO;
import api.services.UserService;
import api.util.UserCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@DisplayName("User Controller Test")
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
    void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0); //NOSONAR
                return "";
            });
            Schedulers.parallel().schedule(task);
            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
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
