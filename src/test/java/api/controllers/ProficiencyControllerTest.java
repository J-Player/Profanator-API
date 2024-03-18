package api.controllers;

import api.controllers.impl.ProficiencyController;
import api.models.dtos.ProficiencyDTO;
import api.models.entities.Proficiency;
import api.services.impl.ProficiencyService;
import api.util.ProficiencyCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

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
        BDDMockito.when(proficiencyService.findById(anyInt()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findByName(anyString()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findAll(any(Pageable.class)))
                .thenReturn(Mono.just(new PageImpl<>(List.of(proficiency))));
        BDDMockito.when(proficiencyService.save(any(Proficiency.class)))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.update(any(Proficiency.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(proficiencyService.delete(anyInt()))
                .thenReturn(Mono.empty());
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
        StepVerifier.create(proficiencyController.findById(1))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("listAll | Returns all proficiencies when successful")
    void listAll() {
        StepVerifier.create(proficiencyController.listAll(Pageable.unpaged()))
                .expectSubscription()
                .expectNext(new PageImpl<>(List.of(proficiency)))
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
        StepVerifier.create(proficiencyController.update(1, proficiencyDTO))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
        StepVerifier.create(proficiencyController.delete(1))
                .expectSubscription()
                .verifyComplete();
    }

}
