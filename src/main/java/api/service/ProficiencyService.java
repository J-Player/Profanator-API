package api.service;

import api.domain.Proficiency;
import api.mapper.ProficiencyMapper;
import api.repository.ProficiencyRepository;
import api.request.post.ProficiencyPostRequestBody;
import api.request.put.ProficiencyPutRequestBody;
import api.service.cache.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static api.config.cache.CacheConfiguration.PROFICIENCY_CACHE_NAME;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = PROFICIENCY_CACHE_NAME)
public class ProficiencyService implements Service<Proficiency, ProficiencyPostRequestBody, ProficiencyPutRequestBody> {

    private static final String CACHE_NAME = PROFICIENCY_CACHE_NAME;
    private final ProficiencyRepository proficiencyRepository;
    private final CacheService cacheService;

    @Override
    @Cacheable(keyGenerator = "customKeyGenerator")
    public Mono<Proficiency> findById(int id) {
        return proficiencyRepository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException(null))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar a Proficiency: {}.", ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(proficiency -> cacheService.getDurationTTL(), ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Cacheable(keyGenerator = "customKeyGenerator")
    public Mono<Proficiency> findByName(String name) {
        return proficiencyRepository.findByNameIgnoreCase(name)
                .switchIfEmpty(monoResponseStatusNotFoundException(name))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar a Proficiency: {}.", ex.getMessage());
                    return Mono.error(ex);
                })
                .cache(proficiency -> cacheService.getDurationTTL(), ex -> Duration.ZERO, () -> Duration.ZERO);
    }

    @Override
    @Cacheable(keyGenerator = "customKeyGenerator")
    public Flux<Proficiency> findAll() {
        return proficiencyRepository.findAll(Sort.by("id"))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao recuperar as Proficiencies: {}.", ex.getMessage());
                    cacheService.evictCache(CACHE_NAME, "findAll");
                    return Mono.error(ex);
                })
                .cache(cacheService.getDurationTTL());
    }

    @Override
    public Mono<Proficiency> save(ProficiencyPostRequestBody proficiencyPostRequestBody) {
        Proficiency proficiency = ProficiencyMapper.INSTANCE.toProficiency(proficiencyPostRequestBody);
        return proficiencyRepository.save(proficiency)
                .doOnSuccess(p -> {
                    if (p != null) cacheService.evictCache(CACHE_NAME, "findAll");
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao salvar a Proficiency ({}): {}", proficiency, ex.getMessage());
                    return Mono.error(ex);
                });
    }

    @Override
    public Mono<Void> update(ProficiencyPutRequestBody proficiencyPutRequestBody) {
        Proficiency newProficiency = ProficiencyMapper.INSTANCE.toProficiency(proficiencyPutRequestBody);
        return findById(newProficiency.getId())
                .flatMap(oldProficiency -> proficiencyRepository.save(newProficiency).thenReturn(oldProficiency))
                .doOnSuccess(p -> {
                    if (p != null) {
                        cacheService.evictCache(CACHE_NAME, "findById", p.getId());
                        cacheService.evictCache(CACHE_NAME, "findByName", p.getName());
                        cacheService.evictCache(CACHE_NAME, "findAll");
                    }
                })
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar a Proficiency ({}): {}", newProficiency, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    public Mono<Void> delete(int id) {
        return findById(id)
                .doOnNext(proficiencyRepository::delete)
                .doOnSuccess(p -> {
                    if (p != null) {
                        cacheService.evictCache(CACHE_NAME, "findById", p.getId());
                        cacheService.evictCache(CACHE_NAME, "findByName", p.getName());
                        cacheService.evictCache(CACHE_NAME, "findAll");
                    }
                })
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
