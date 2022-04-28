package api.controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AbstractControllerImpl<T, Id> {

    Flux<T> listAll();

    Mono<T> findById(Id id);

    Mono<T> save(T t);

    Mono<Void> update(T t);

    Mono<Void> delete(Id id);

}
