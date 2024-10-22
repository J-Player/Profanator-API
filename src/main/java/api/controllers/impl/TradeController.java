package api.controllers.impl;

import api.annotations.PageableAsQueryParam;
import api.controllers.IController;
import api.mappers.TradeMapper;
import api.models.dtos.TradeDTO;
import api.models.entities.Trade;
import api.services.impl.TradeService;
import api.services.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/trade")
@Tag(name = "Trade")
public class TradeController implements IController<Trade, TradeDTO> {

    private final TradeService tradeService;
    private final UserService userService;

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a trade by ID.")
    public Mono<Trade> findById(@PathVariable Integer id) {
        return tradeService.findById(id);
    }

    @GetMapping("/all")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a list of trades.")
    public Mono<Page<Trade>> findAll(@RequestParam String item,
                                     @RequestParam(required = false) String seller,
                                     @RequestParam(required = false, name = "min_price") Integer minPrice,
                                     @RequestParam(required = false, name = "max_price") Integer maxPrice,
                                     @Parameter(hidden = true) Pageable pageable) {
        return tradeService.findAll(item, seller, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{seller}/all")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return a list of trades from a seller.")
    public Mono<Page<Trade>> findAllBySeller(@PathVariable String seller,
                                             @Parameter(hidden = true) Pageable pageable) {
        return tradeService.findAllBySeller(seller, pageable);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a trade in the database.")
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.username == #tradeDTO.seller")
    public Mono<Trade> save(@RequestBody TradeDTO tradeDTO) {
        Trade trade = TradeMapper.INSTANCE.toTrade(tradeDTO);
        return tradeService.save(trade);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a trade in the database.")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tradeController.isOwner(#id, authentication.principal.id)")
    public Mono<Void> update(@PathVariable Integer id, @RequestBody TradeDTO tradeDTO) {
        Trade trade = TradeMapper.INSTANCE.toTrade(tradeDTO);
        return tradeService.update(trade.withId(id));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a trade in the database.")
    @PreAuthorize("hasRole('ROLE_ADMIN') or @tradeController.isOwner(#id, authentication.principal.id)")
    public Mono<Void> delete(@PathVariable Integer id) {
        return tradeService.delete(id);
    }

    @SuppressWarnings("unused")
    private Mono<Boolean> isOwner(Integer tradeId, Integer userId) {
        return tradeService.findById(tradeId)
                .log()
                .map(Trade::getSeller)
                .flatMap(userService::findByName)
                .map(user -> user.getId().equals(userId));
    }

}
