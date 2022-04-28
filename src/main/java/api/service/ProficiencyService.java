package api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import api.domain.Proficiency;
import api.repository.ProficiencyRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProficiencyService implements AbstractServiceImpl<Proficiency, Integer> {

    private final ProficiencyRepository proficiencyRepository;

    public Mono<Proficiency> findByName(String name) {
        return proficiencyRepository.findByNameIgnoreCase(name)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Mono<Proficiency> findById(Integer id) {
        return proficiencyRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public Flux<Proficiency> findAll() {
        return proficiencyRepository.findAll();
    }

    public Mono<Proficiency> save(Proficiency proficiency) {
        return proficiencyRepository.save(proficiency);
    }

    public Mono<Void> update(Proficiency proficiency) {
        return findById(proficiency.getId())
                .flatMap(proficiencyRepository::save)
                .then();
    }

    public Mono<Void> delete(Integer id) {
        return findById(id)
                .flatMap(proficiencyRepository::delete);
    }

    private <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Proficiency not found"));
    }

}
