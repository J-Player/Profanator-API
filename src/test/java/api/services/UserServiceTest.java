package api.services;

import api.domains.User;
import api.repositories.UserRepository;
import api.util.UserCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
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
@DisplayName("User Service Test")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final User user = UserCreator.user();

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
        BDDMockito.when(userRepository.findById(any(UUID.class)))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userRepository.findByUsername(anyString()))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userRepository.findAll())
                .thenReturn(Flux.just(user));
        BDDMockito.when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userRepository.delete(any(User.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a mono of user when successful")
    void findById() {
        StepVerifier.create(userService.findById(UUID.randomUUID()))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when user does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.findById(UUID.randomUUID()))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findByName | Returns a mono of user when successful")
    void findByName() {
        StepVerifier.create(userService.findByName(""))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName | Returns a mono error when user does not exists")
    void findByName_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findByUsername(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.findByName(""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAll | Returns a flux of user when successful")
    void findAll() {
        StepVerifier.create(userService.findAll())
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("save | creates an user when successful")
    void save() {
        StepVerifier.create(userService.save(user))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | save updated user and returns empty mono when successful")
    void update() {
        StepVerifier.create(userService.update(user))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when user does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.update(user))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | removes the user when successful")
    void delete() {
        StepVerifier.create(userService.delete(UUID.randomUUID()))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns mono error when user does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.delete(UUID.randomUUID()))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}