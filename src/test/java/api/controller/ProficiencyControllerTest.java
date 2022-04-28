package api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import api.domain.Proficiency;
import api.service.ProficiencyService;
import api.util.ProficiencyCreator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@DisplayName("Proficiency Controller Test")
public class ProficiencyControllerTest {

    @InjectMocks
    private ProficiencyController proficiencyController;

    @Mock
    private ProficiencyService proficiencyService;

    private final Proficiency proficiency = ProficiencyCreator.createValidProficiency();

    @BeforeEach
    void setUp() {
        BDDMockito.when(proficiencyService.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findAll())
                .thenReturn(Flux.just(proficiency));

        //SAVE
        BDDMockito.when(proficiencyService.save(ArgumentMatchers.any(Proficiency.class)))
                .thenReturn(Mono.just(proficiency));
        //UPDATE
        BDDMockito.when(proficiencyService.update(ArgumentMatchers.any(Proficiency.class)))
                .thenReturn(Mono.empty());
        //DELETE
        BDDMockito.when(proficiencyService.delete(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

    }

    @Test
    void findByName() {
        StepVerifier.create(proficiencyController.findByName(""))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    void findById() {
        StepVerifier.create(proficiencyController.findById(1))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    void listAll() {
        StepVerifier.create(proficiencyController.listAll())
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    void save() {
        Proficiency proficiencyToBeSaved = ProficiencyCreator.createProficiencyToBeSaved();
        StepVerifier.create(proficiencyController.save(proficiencyToBeSaved))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    void update() {
        Proficiency proficiencyToBeUpdated = ProficiencyCreator.createProficiencyToBeUpdated();
        StepVerifier.create(proficiencyController.update(proficiencyToBeUpdated))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void delete() {
        StepVerifier.create(proficiencyController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

}
