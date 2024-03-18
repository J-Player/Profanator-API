package api.services;

import api.models.entities.Proficiency;
import api.repositories.impl.ProficiencyRepository;
import api.services.cache.CacheService;
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
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Proficiency Service Test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ProficiencyServiceTest {

    @InjectMocks
    private ProficiencyService proficiencyService;

    @Mock
    private ProficiencyRepository proficiencyRepository;

    @Mock
    private CacheService cacheService;

    private final Proficiency proficiency = ProficiencyCreator.proficiency().withId(1);

    @BeforeEach
    void setUp() {
        BDDMockito.when(proficiencyRepository.findByNameIgnoreCase(anyString()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.findById(anyInt()))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.findAllBy(any(Pageable.class)))
                .thenReturn(Flux.just(proficiency));
        BDDMockito.when(proficiencyRepository.count())
                .thenReturn(Mono.just(1L));
        BDDMockito.when(proficiencyRepository.save(any(Proficiency.class)))
                .thenReturn(Mono.just(proficiency));
        BDDMockito.when(proficiencyRepository.save(proficiency))
                .thenReturn(Mono.empty());
        BDDMockito.when(proficiencyRepository.delete(any(Proficiency.class)))
                .thenReturn(Mono.empty());
        BDDMockito.when(proficiencyRepository.delete(any(Proficiency.class)))
                .thenReturn(Mono.empty());
        BDDMockito.doNothing().when(cacheService).evictCache(anyString(), anyString(), any(Pageable.class));
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
        Pageable pageable = Pageable.ofSize(100);
        List<Proficiency> proficiencyList = List.of(proficiency);
        StepVerifier.create(proficiencyService.findAll(pageable))
                .expectSubscription()
                .expectNext(new PageImpl<>(proficiencyList, pageable, proficiencyList.size()))
                .verifyComplete();
    }

    @Test
    @DisplayName("save | Create a proficiency in database")
    void save() {
        StepVerifier.create(proficiencyService.save(proficiency.withId(null)))
                .expectSubscription()
                .expectNext(proficiency)
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Save updated proficiency and returns empty mono when successful")
    void update() {
        StepVerifier.create(proficiencyService.update(proficiency))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("update | Returns mono error when proficiency does not exists")
    void update_ReturnsMonoError_WhenEmptyMonoIsReturned() {
        BDDMockito.when(proficiencyRepository.findById(anyInt()))
                .thenReturn(Mono.empty());
        StepVerifier.create(proficiencyService.update(proficiency))
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
