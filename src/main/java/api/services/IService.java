package api.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IService<T> {

    Mono<T> findById(Long id);

    Flux<T> findAll();

    Mono<T> save(T t);

    Mono<Void> update(T t);

    Mono<Void> delete(Long id);

}
