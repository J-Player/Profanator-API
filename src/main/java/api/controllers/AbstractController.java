package api.controllers;

import reactor.core.publisher.Mono;

import java.util.UUID;

interface AbstractController<T1, T2> {

    Mono<T1> findById(UUID id);
    Mono<T1> save(T2 t2);
    Mono<Void> update(UUID id, T2 t2);
    Mono<Void> delete(UUID id);

}
