package api.controller;

import api.domain.Item;
import api.request.post.ItemPostRequestBody;
import api.request.put.ItemPutRequestBody;
import api.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@SecurityScheme(name = "Basic Authentication", type = SecuritySchemeType.HTTP, scheme = "basic")
public class ItemController implements Controller<Item, ItemPostRequestBody, ItemPutRequestBody> {

    private final ItemService itemService;

    @Override
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a item by ID.", tags = {"item"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Item> findById(@PathVariable int id) {
        return itemService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a item by name.", tags = {"item"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Item> findByName(@RequestParam String name) {
        return itemService.findByName(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all items.", tags = {"item"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Flux<Item> listAll(@RequestParam(required = false) String proficiency) {
        return proficiency != null ? itemService.findAllByProficiency(proficiency) : itemService.findAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a item in the database.", tags = {"item"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Item> save(@RequestBody @Valid ItemPostRequestBody item) {
        return itemService.save(item);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a item in the database.", tags = {"item"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Void> update(@RequestBody @Valid ItemPutRequestBody item) {
        return itemService.update(item);
    }

    @Override
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a item in the database.", tags = {"item"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Void> delete(@PathVariable int id) {
        return itemService.delete(id);
    }

}
