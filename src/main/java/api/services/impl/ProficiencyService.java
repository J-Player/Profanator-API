package api.services.impl;

import api.domains.Proficiency;
import api.repositories.ProficiencyRepository;
import api.services.IService;
import api.services.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static api.configs.cache.CacheConfig.PROFICIENCY_CACHE_NAME;
import static api.configs.cache.CacheConfig.TTL;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = PROFICIENCY_CACHE_NAME)
public class ProficiencyService implements IService<Proficiency> {

    private final ProficiencyRepository proficiencyRepository;
    private final CacheService cacheService;

    @Override
    @Cacheable
    public Mono<Proficiency> findById(Long id) {
        return proficiencyRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException(null))
                .cache(proficiency -> TTL, ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Cacheable
    public Mono<Proficiency> findByName(String name) {
        return proficiencyRepository.findByNameIgnoreCase(name)
                .switchIfEmpty(monoResponseStatusNotFoundException(name))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar a Proficiency: {}.", ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(proficiency -> TTL, ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Override
    @Cacheable
    public Flux<Proficiency> findAll() {
        return proficiencyRepository.findAll(Sort.by("name"))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar as Proficiencies: {}.", ex.getMessage());
                    cacheService.evictCache(PROFICIENCY_CACHE_NAME);
                    return Mono.error(ex);
                })
                .cache(TTL);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Mono<Proficiency> save(Proficiency proficiency) {
        return proficiencyRepository.save(proficiency)
                .doOnNext(p -> {
                    log.info("Proficiency salva com sucesso! ({}).", p);
                    cacheService.evictCache(PROFICIENCY_CACHE_NAME, "findAll");
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar a Proficiency ({}): {}", proficiency, ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    @CacheEvict(allEntries = true)
    public Mono<Void> update(Proficiency proficiency) {
        return findById(proficiency.getId())
                .doOnNext(oldProficiency -> {
                    proficiency.setCreatedAt(oldProficiency.getCreatedAt());
                    proficiency.setUpdatedAt(oldProficiency.getUpdatedAt());
                    proficiency.setVersion(oldProficiency.getVersion());
                })
                .flatMap(oldProficiency -> proficiencyRepository.save(proficiency)
                        .doOnNext(p -> log.info("Proficiency atualizada com sucesso! {}", p)))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar a Proficiency ({}): {}", proficiency, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    @CacheEvict(allEntries = true)
    public Mono<Void> delete(Long id) {
        return findById(id)
                .flatMap(proficiency -> proficiencyRepository.delete(proficiency).thenReturn(proficiency))
                .doOnSuccess(proficiency -> log.info("Proficiency excluída com sucesso! ({})", proficiency))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao excluir a Proficiency (id: {}): {}.", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    private <T> Mono<T> monoResponseStatusNotFoundException(String proficiency) {
        String message = proficiency != null && proficiency.length() > 0 ?
                String.format("Proficiency '%s' not found", proficiency) : "Proficiency not found";
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, message));
    }

}
