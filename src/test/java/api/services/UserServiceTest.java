package api.services;

import api.domains.User;
import api.domains.dtos.UserDTO;
import api.repositories.UserRepository;
import api.services.impl.UserService;
import api.utils.UserCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("User Service Test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final User user = UserCreator.user();
    private final UserDTO userDTO = UserCreator.userDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(userRepository.findById(anyLong()))
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
        StepVerifier.create(userService.findById(1L))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when user does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.findById(1L))
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
        StepVerifier.create(userService.save(userDTO))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | save updated user and returns empty mono when successful")
    void update() {
        StepVerifier.create(userService.update(userDTO, 1L))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when user does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.update(userDTO, 1L))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | removes the user when successful")
    void delete() {
        StepVerifier.create(userService.delete(1L))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns mono error when user does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findById(anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.delete(1L))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}