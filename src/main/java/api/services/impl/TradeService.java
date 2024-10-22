package api.services.impl;

import api.exceptions.ResourceNotFoundException;
import api.models.entities.Trade;
import api.repositories.impl.TradeRepository;
import api.services.IService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService implements IService<Trade> {

    private final TradeRepository tradeRepository;
    private final UserService userService;

    @Override
    public Mono<Trade> findById(Integer id) {
        return tradeRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Trade not found")));
    }

    @Override
    public Mono<Page<Trade>> findAll(Pageable pageable) {
        return tradeRepository.findAllBy(pageable)
                .collectList()
                .zipWith(tradeRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
    }

    public Mono<Page<Trade>> findAllBySeller(String seller, Pageable pageable) {
        return userService.findByName(seller)
                .then(tradeRepository.findAllBySellerIgnoreCase(seller, pageable)
                        .collectList()
                        .zipWith(tradeRepository.count())
                        .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2())));
    }

    public Mono<Page<Trade>> findAll(String item, String seller, Integer minPrice, Integer maxPrice, Pageable pageable) {
        Predicate<Object[]> isAllNonNull = objects -> Arrays.stream(objects).allMatch(Objects::nonNull);
        Function<Flux<Trade>, Mono<Page<Trade>>> fluxMonoFunction = flux -> flux
                .collectList()
                .zipWith(tradeRepository.count())
                .map(p -> new PageImpl<>(p.getT1(), pageable, p.getT2()));
        if (isAllNonNull.test(new Object[]{seller, minPrice, maxPrice})) {
            return userService.findByName(seller)
                    .then(fluxMonoFunction.apply(
                            tradeRepository
                                    .findAllByItemIgnoreCaseAndSellerIgnoreCaseAndPriceBetween(item, seller, minPrice, maxPrice, pageable)));
        } else if (seller != null && (minPrice != null ^ maxPrice != null)) {
            return userService.findByName(seller)
                    .then(fluxMonoFunction.apply(minPrice != null ?
                            tradeRepository
                                    .findAllByItemIgnoreCaseAndSellerIgnoreCaseAndPriceGreaterThanEqual(item, seller, minPrice, pageable) :
                            tradeRepository
                                    .findAllByItemIgnoreCaseAndSellerIgnoreCaseAndPriceLessThanEqual(item, seller, maxPrice, pageable)));
        } else if (seller != null) {
            return userService.findByName(seller)
                    .then(fluxMonoFunction.apply(
                            tradeRepository
                                    .findAllByItemIgnoreCaseAndSellerIgnoreCase(item, seller, pageable)));
        } else if (isAllNonNull.test(new Object[]{minPrice, maxPrice})) {
            return fluxMonoFunction.apply(
                    tradeRepository
                            .findAllByItemIgnoreCaseAndPriceBetween(item, minPrice, maxPrice, pageable));
        } else if (minPrice != null || maxPrice != null) {
            return fluxMonoFunction.apply(minPrice != null ?
                    tradeRepository
                            .findAllByItemIgnoreCaseAndPriceGreaterThanEqual(item, minPrice, pageable) :
                    tradeRepository
                            .findAllByItemIgnoreCaseAndPriceLessThanEqual(item, maxPrice, pageable));
        } else {
            return fluxMonoFunction.apply(tradeRepository.findAllByItem(item, pageable));
        }
    }

    @Override
    public Mono<Trade> save(Trade trade) {
        return tradeRepository.save(trade)
                .doOnNext(i -> log.info("Trade salvo com sucesso! {}", i))
                .doOnError(ex -> log.error("Ocorreu um erro ao salvar um trade: {}", ex.getMessage()));
    }

    @Override
    public Mono<Void> update(Trade trade) {
        return findById(trade.getId())
                .filter(trade1 -> trade1.getSeller().equalsIgnoreCase(trade.getSeller()))
                .doOnNext(oldTrade -> {
                    trade.setCreatedAt(oldTrade.getCreatedAt());
                    trade.setUpdatedAt(oldTrade.getUpdatedAt());
                })
                .flatMap(tradeRepository::save)
                .doOnNext(i -> log.info("Trade atualizado com sucesso! {}", i))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao atualizar o trade (id: {}): {}", trade.getId(), ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return findById(id)
                .flatMap(item -> tradeRepository.delete(item).thenReturn(item))
                .doOnSuccess(item -> log.info("Trade excluído com sucesso! ({})", item))
                .onErrorResume(ex -> {
                    log.error("Ocorreu um erro ao excluir o trade (id: {}): {}", id, ex.getMessage());
                    return Mono.error(ex);
                })
                .then();
    }
}
