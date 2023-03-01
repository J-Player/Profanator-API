package api.controllers;

import api.configs.BlockHoundTest;
import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
import api.services.impl.ProficiencyService;
import api.util.ProficiencyCreator;
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
@DisplayName("Proficiency Controller Test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ProficiencyControllerTest {

    @InjectMocks
    private ProficiencyController proficiencyController;

    @Mock
    private ProficiencyService proficiencyService;

    private final Proficiency proficiency = ProficiencyCreator.proficiency();
    private final ProficiencyDTO proficiencyDTO = ProficiencyCreator.proficiencyDTO();

    @BeforeEach
    void setUp() {
        BDDMockito.when(proficiencyService.findById(any(UUID.class)))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findByName(anyString()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findAll())
                .thenReturn(Flux.just(proficiency));
        BDDMockito.when(proficiencyService.save(any(Proficiency.class)))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.update(any(Proficiency.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(proficiencyService.delete(any(UUID.class)))
                .thenReturn(Mono.empty());
    }

    @BeforeAll
    static void blockHound() {
        BlockHound.install();
    }

    @Test
    @DisplayName("[BlockHound] Check if BlockHound is working")
    void blockHoundWorks() {
        BlockHoundTest.test();
    }

    @Test
    @DisplayName("findByName | Returns a proficiency when successful")
    void findByName() {
        StepVerifier.create(proficiencyController.findByName(""))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById | Returns a proficiency when successful")
    void findById() {
        StepVerifier.create(proficiencyController.findById(UUID.randomUUID()))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAll | Returns all proficiencies when successful")
    void listAll() {
        StepVerifier.create(proficiencyController.listAll())
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Returns a proficiency when successful")
    void save() {
        StepVerifier.create(proficiencyController.save(proficiencyDTO))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns status 204 (no content) when successful")
    void update() {
        StepVerifier.create(proficiencyController.update(UUID.randomUUID(), proficiencyDTO))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(proficiencyController.delete(UUID.randomUUID()))
                .expectSubscription()
                .verifyComplete();
    }

}
