package api.controllers;

import api.domains.Item;
import api.domains.dtos.ItemDTO;
import api.services.impl.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Tag(name = "Item")
@SecurityScheme(name = "Basic Authentication", type = SecuritySchemeType.HTTP, scheme = "basic")
@SecurityRequirement(name = "Basic Authentication")
public class ItemController implements IController<Item, ItemDTO> {

    private final ItemService itemService;

    @Override
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a item by ID.")
    public Mono<Item> findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a item by name.")
    public Mono<Item> findByName(@RequestParam String name) {
        return itemService.findByName(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all items.")
    public Flux<Item> listAll(@RequestParam(required = false) String proficiency) {
        return proficiency != null ? itemService.findAllByProficiency(proficiency) : itemService.findAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a item in the database.")
    public Mono<Item> save(@RequestBody @Valid ItemDTO itemDTO) {
        return itemService.save(itemDTO);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a item in the database.")
    public Mono<Void> update(@RequestBody @Valid ItemDTO itemDTO, @PathVariable Long id) {
        return itemService.update(itemDTO, id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a item in the database.")
    public Mono<Void> delete(@PathVariable Long id) {
        return itemService.delete(id);
    }

}
