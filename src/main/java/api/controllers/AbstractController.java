package api.controllers;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AbstractController<T, DTO> {

    Mono<T> findById(UUID id);
    Mono<T> save(DTO dto);
    Mono<Void> update(UUID id, DTO dto);
    Mono<Void> delete(UUID id);

}
