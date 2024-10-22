package api.repositories.impl;

import api.models.entities.Trade;
import api.repositories.IRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TradeRepository extends IRepository<Trade> {

    Flux<Trade> findAllByItem(String item, Pageable pageable);
    Flux<Trade> findAllBySellerIgnoreCase(String seller, Pageable pageable);
    Flux<Trade> findAllByItemIgnoreCaseAndPriceGreaterThanEqual(String item, Integer price, Pageable pageable);
    Flux<Trade> findAllByItemIgnoreCaseAndPriceLessThanEqual(String item, Integer price, Pageable pageable);
    Flux<Trade> findAllByItemIgnoreCaseAndSellerIgnoreCaseAndPriceBetween(String item, String seller, Integer minPrice, Integer maxPrice, Pageable pageable);

    Flux<Trade> findAllByItemIgnoreCaseAndSellerIgnoreCaseAndPriceLessThanEqual(String item, String seller, Integer maxPrice, Pageable pageable);
    Flux<Trade> findAllByItemIgnoreCaseAndSellerIgnoreCaseAndPriceGreaterThanEqual(String item, String seller, Integer minPrice, Pageable pageable);
    Flux<Trade> findAllByItemIgnoreCaseAndSellerIgnoreCase(String item, String seller, Pageable pageable);
    Flux<Trade> findAllByItemIgnoreCaseAndPriceBetween(String item, Integer minPrice, Integer maxPrice, Pageable pageable);

}
