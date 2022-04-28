package api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import api.domain.Item;
import api.service.ItemService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@SecurityScheme(
        name = "Basic Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "basic")
public class ItemController implements AbstractControllerImpl<Item, Integer> {

    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a item by name.", tags = {"item"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Item> findByName(@RequestParam String name) {
        return itemService.findByName(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all items.", tags = {"item"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Flux<Item> listAll() {
        return itemService.findAll();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a item by ID.", tags = {"item"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Item> findById(@PathVariable Integer id) {
        return itemService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a item in the database.", tags = {"item"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Item> save(@RequestBody @Valid Item item) {
        return itemService.save(item);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a item in the database.", tags = {"item"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> update(@RequestBody @Valid Item item) {
        return itemService.update(item);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a item in the database.", tags = {"item"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> delete(@PathVariable Integer id) {
        return itemService.delete(id);
    }

}
