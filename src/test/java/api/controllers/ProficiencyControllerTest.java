package api.controllers;

<<<<<<< HEAD
import api.controllers.impl.ProficiencyController;
import api.models.dtos.ProficiencyDTO;
import api.models.entities.Proficiency;
=======
import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
>>>>>>> main
import api.services.impl.ProficiencyService;
import api.utils.ProficiencyCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
<<<<<<< HEAD
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

=======
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

>>>>>>> main
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Proficiency Controller Test")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ProficiencyControllerTest {

    @InjectMocks
    private ProficiencyController proficiencyController;

    @Mock
    private ProficiencyService proficiencyService;

    private final Proficiency proficiency = ProficiencyCreator.proficiency();
    private final ProficiencyDTO proficiencyDTO = ProficiencyCreator.proficiencyDTO();

    @BeforeEach
    void setUp() {
<<<<<<< HEAD
        BDDMockito.when(proficiencyService.findById(anyInt()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findByName(anyString()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findAll(any(Pageable.class)))
                .thenReturn(Mono.just(new PageImpl<>(List.of(proficiency))));
        BDDMockito.when(proficiencyService.save(any(Proficiency.class)))
=======
        BDDMockito.when(proficiencyService.findById(anyLong()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findByName(anyString()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.findAll())
                .thenReturn(Flux.just(proficiency));
        BDDMockito.when(proficiencyService.save(any(ProficiencyDTO.class)))
>>>>>>> main
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyService.update(any(ProficiencyDTO.class), anyLong()))
                .thenReturn(Mono.empty());
<<<<<<< HEAD
        BDDMockito.when(proficiencyService.delete(anyInt()))
=======
        BDDMockito.when(proficiencyService.delete(anyLong()))
>>>>>>> main
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
<<<<<<< HEAD
        StepVerifier.create(proficiencyController.findById(1))
=======
        StepVerifier.create(proficiencyController.findById(1L))
>>>>>>> main
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
<<<<<<< HEAD
        StepVerifier.create(proficiencyController.update(1, proficiencyDTO))
=======
        StepVerifier.create(proficiencyController.update(proficiencyDTO, 1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete | Returns status 204 (no content) when successful")
    void delete() {
<<<<<<< HEAD
        StepVerifier.create(proficiencyController.delete(1))
=======
        StepVerifier.create(proficiencyController.delete(1L))
>>>>>>> main
                .expectSubscription()
                .verifyComplete();
    }

}
