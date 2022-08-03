package api.service;

import api.domain.Proficiency;
import api.repository.ProficiencyRepository;
import api.request.post.ProficiencyPostRequestBody;
import api.request.put.ProficiencyPutRequestBody;
import api.service.cache.CacheService;
import api.util.ProficiencyCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Proficiency Service Test")
class ProficiencyServiceTest {

    @InjectMocks
    private ProficiencyService proficiencyService;

    @Mock
    private ProficiencyRepository proficiencyRepository;

    @Mock
    private CacheService cacheService;

    @Spy
    private CacheManager cacheManager;

    private final Proficiency proficiency = ProficiencyCreator.proficiency();
    private final ProficiencyPostRequestBody proficiencyToSaved = ProficiencyCreator.proficiencyToSave();
    private final ProficiencyPutRequestBody proficiencyToUpdated = ProficiencyCreator.proficiencyToUpdate();

    @BeforeAll
    static void blockHound() {
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
        BDDMockito.when(proficiencyRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.findById(anyInt()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.findAll(any(Sort.class)))
                .thenReturn(Flux.just(proficiency));
        BDDMockito.when(proficiencyRepository.save(any(Proficiency.class)))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.save(proficiency))
                .thenReturn(Mono.empty());
        BDDMockito.when(proficiencyRepository.delete(any(Proficiency.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(cacheService.getDurationTTL())
                .thenReturn(Duration.ofSeconds(60));
//        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("findById | Returns a mono of proficiency")
    void findById_ReturnMonoProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.findById(1))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns mono error when proficiency does not exists")
    void findById_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(proficiencyRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(proficiencyService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findByName | Returns a mono of proficiency")
    void findByName_ReturnMonoProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.findByName(""))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName | Returns mono error when proficiency does not exists")
    void findByName_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(proficiencyRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Mono.empty());
        StepVerifier.create(proficiencyService.findByName(""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAll | Returns a flux of proficiency")
    void findAll_ReturnFluxOfProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.findAll())
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Create a proficiency in database")
    void save() {
        StepVerifier.create(proficiencyService.save(proficiencyToSaved))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | save updated proficiency and returns empty mono when successful")
    void update() {
        StepVerifier.create(proficiencyService.update(proficiencyToUpdated))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when proficiency does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(proficiencyRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(proficiencyService.update(proficiencyToUpdated))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("delete | Removes the proficiency when successful")
    void delete() {
        StepVerifier.create(proficiencyService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns mono error when proficiency does not exist")
    void delete_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(proficiencyRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(proficiencyService.delete(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

}
