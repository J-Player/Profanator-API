package api.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import api.domain.Proficiency;
import api.repository.ProficiencyRepository;
import api.util.ProficiencyCreator;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
public class ProficiencyServiceTest {

    @InjectMocks
    private ProficiencyService proficiencyService;

    @Mock
    private ProficiencyRepository proficiencyRepository;

    private final Proficiency proficiency = ProficiencyCreator.createValidProficiency();

    @BeforeAll
    public static void blockHound() {
        BlockHound.install();
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
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
        BDDMockito.when(proficiencyRepository.findByNameIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.findAll())
                .thenReturn(Flux.just(proficiency));

        //SAVE
        BDDMockito.when(proficiencyRepository.save(ProficiencyCreator.createProficiencyToBeSaved()))
                        .thenReturn(Mono.just(proficiency));
        //UPDATE
        BDDMockito.when(proficiencyRepository.save(ProficiencyCreator.createValidProficiency()))
                .thenReturn(Mono.empty());
        //DELETE
        BDDMockito.when(proficiencyRepository.delete(ArgumentMatchers.any(Proficiency.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("findById returns a mono of proficiency")
    public void findById_ReturnMonoProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.findById(1))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns mono error when proficiency does not exists")
    public void findById_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(proficiencyRepository.findById(ArgumentMatchers.anyInt()))
                        .thenReturn(Mono.empty());
        StepVerifier.create(proficiencyService.findById(1))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findByName returns a mono of proficiency")
    public void findByName_ReturnMonoProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.findByName(""))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByName returns mono error when proficiency does not exists")
    public void findByName_ReturnMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(proficiencyRepository.findByNameIgnoreCase(ArgumentMatchers.anyString()))
                        .thenReturn(Mono.empty());
        StepVerifier.create(proficiencyService.findByName(""))
                .expectSubscription()
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    @DisplayName("findAll returns a flux of proficiency")
    public void findAll_ReturnFluxOfProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.findAll())
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("save create a proficiency in database")
    public void save_CreateProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.save(ProficiencyCreator.createProficiencyToBeSaved()))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update replace a proficiency from database")
    public void update_SaveUpdatedProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.update(ProficiencyCreator.createProficiencyToBeUpdated()))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete remove a proficiency from database")
    public void delete_RemovesProficiency_WhenSuccessful() {
        StepVerifier.create(proficiencyService.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

}
