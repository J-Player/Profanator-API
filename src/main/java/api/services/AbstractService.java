package api.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AbstractService<T> {

    Mono<T> findById(UUID id);

    Flux<T> findAll();

    Mono<T> save(T t);

    Mono<Void> update(T t);

    Mono<Void> delete(UUID id);

}
