package api.services;

import api.exceptions.ResourceNotFoundException;
import api.models.entities.User;
import api.repositories.impl.UserRepository;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("User Service Test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final User user = UserCreator.user().withId(1);

    @BeforeEach
    void setUp() {
        BDDMockito.when(userRepository.findById(anyInt()))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userRepository.findByUsernameIgnoreCase(anyString()))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userRepository.findAllBy(any(Pageable.class)))
                .thenReturn(Flux.just(user));
        BDDMockito.when(userRepository.count())
                .thenReturn(Mono.just(1L));
        BDDMockito.when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(user));
        BDDMockito.when(userRepository.delete(any(User.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById | Returns a mono of user when successful")
    void findById() {
        StepVerifier.create(userService.findById(1))
                .expectSubscription()
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a mono error when user does not exists")
    void findById_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.findById(1))
                .expectSubscription()
                .expectError(ResourceNotFoundException.class)
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
        BDDMockito.when(userRepository.findByUsernameIgnoreCase(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.findByName(""))
                .expectSubscription()
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("findAll | Returns a flux of user when successful")
    void findAll() {
        Pageable pageable = Pageable.ofSize(100);
        List<User> userList = List.of(this.user);
        StepVerifier.create(userService.findAll(pageable))
                .expectSubscription()
                .expectNext(new PageImpl<>(userList, pageable, userList.size()))
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
        BDDMockito.when(userRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.update(user))
                .expectSubscription()
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | removes the user when successful")
    void delete() {
        StepVerifier.create(userService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("delete | Returns mono error when user does not exists")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(userRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(userService.delete(1))
                .expectSubscription()
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

}