package api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

<<<<<<< HEAD
public interface IService<T> {

    Mono<T> findById(Integer id);

    Mono<Page<T>> findAll(Pageable pageable);
=======
public interface IService<T1, T2> {

    Mono<T1> findById(Long id);

    Flux<T1> findAll();

    Mono<T1> save(T2 t2);
>>>>>>> main

    Mono<Void> update(T2 t2, Long id);

<<<<<<< HEAD
    Mono<Void> update(T t);

    Mono<Void> delete(Integer id);
=======
    Mono<Void> delete(Long id);
>>>>>>> main

}
