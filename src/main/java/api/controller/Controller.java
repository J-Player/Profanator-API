package api.controller;

import reactor.core.publisher.Mono;

public interface Controller<T, T1, T2> {

    Mono<T> findById(int id);
    Mono<T> save(T1 t1);
    Mono<Void> update(T2 t2);
    Mono<Void> delete(int id);

}
