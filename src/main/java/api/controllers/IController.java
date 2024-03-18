package api.controllers;

import reactor.core.publisher.Mono;

<<<<<<< HEAD
public interface IController<T1, T2> {

    Mono<T1> findById(Integer id);
    Mono<T1> save(T2 t2);
    Mono<Void> update(Integer id, T2 t2);
    Mono<Void> delete(Integer id);
=======
interface IController<T1, T2> {

    Mono<T1> findById(Long id);
    Mono<T1> save(T2 t2);
    Mono<Void> update(T2 t2, Long id);
    Mono<Void> delete(Long id);
>>>>>>> main

}
