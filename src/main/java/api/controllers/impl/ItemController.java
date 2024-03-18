package api.controllers.impl;

<<<<<<< HEAD:src/main/java/api/controllers/impl/ItemController.java
import api.annotations.PageableAsQueryParam;
import api.controllers.IController;
import api.mappers.ItemMapper;
import api.models.dtos.ItemDTO;
import api.models.entities.Item;
=======
import api.domains.Item;
import api.domains.dtos.ItemDTO;
>>>>>>> main:src/main/java/api/controllers/ItemController.java
import api.services.impl.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
@Tag(name = "Item")
public class ItemController implements IController<Item, ItemDTO> {

    private final ItemService itemService;

    @Override
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a item by ID.")
<<<<<<< HEAD:src/main/java/api/controllers/impl/ItemController.java
    public Mono<Item> findById(@PathVariable Integer id) {
=======
    public Mono<Item> findById(@PathVariable Long id) {
>>>>>>> main:src/main/java/api/controllers/ItemController.java
        return itemService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a item by name.")
    public Mono<Item> findByName(@RequestParam String name) {
        return itemService.findByName(name);
    }

    @GetMapping("/all")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all items.")
    public Mono<Page<Item>> listAll(@RequestParam(required = false) String proficiency,
                                    @Parameter(hidden = true) Pageable pageable) {
        return (proficiency != null ? itemService.findAllByProficiency(proficiency, pageable) : itemService.findAll(pageable));
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
<<<<<<< HEAD:src/main/java/api/controllers/impl/ItemController.java
    public Mono<Void> update(@PathVariable Integer id, @RequestBody @Valid ItemDTO itemDTO) {
        Item item = ItemMapper.INSTANCE.toItem(itemDTO);
        return itemService.update(item.withId(id));
=======
    public Mono<Void> update(@RequestBody @Valid ItemDTO itemDTO, @PathVariable Long id) {
        return itemService.update(itemDTO, id);
>>>>>>> main:src/main/java/api/controllers/ItemController.java
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a item in the database.")
<<<<<<< HEAD:src/main/java/api/controllers/impl/ItemController.java
    public Mono<Void> delete(@PathVariable Integer id) {
=======
    public Mono<Void> delete(@PathVariable Long id) {
>>>>>>> main:src/main/java/api/controllers/ItemController.java
        return itemService.delete(id);
    }

}
