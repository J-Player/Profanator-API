package api.controllers.impl;

import api.annotations.PageableAsQueryParam;
import api.controllers.IController;
import api.mappers.IngredientMapper;
import api.models.dtos.IngredientDTO;
import api.models.entities.Ingredient;
import api.services.impl.IngredientService;
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
@RequestMapping("/ingredients")
@Tag(name = "Ingredient")
public class IngredientController implements IController<Ingredient, IngredientDTO> {

    private final IngredientService ingredientService;

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a ingredient by id.")
    public Mono<Ingredient> findById(@PathVariable Integer id) {
        return ingredientService.findById(id);
    }

    @GetMapping("/all")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all ingredients.")
    public Mono<Page<Ingredient>> listAll(@RequestParam(required = false) String item, @Parameter(hidden = true) Pageable pageable) {
        return item != null ? ingredientService.findAllByProduct(item, pageable) : ingredientService.findAll(pageable);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a ingredient in the database.")
    public Mono<Ingredient> save(@RequestBody @Valid IngredientDTO ingredientDTO) {
        Ingredient ingredient = IngredientMapper.INSTANCE.toIngredient(ingredientDTO);
        return ingredientService.save(ingredient);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a ingredient in the database.")
    public Mono<Void> update(@PathVariable Integer id, @RequestBody @Valid IngredientDTO ingredientDTO) {
        Ingredient ingredient = IngredientMapper.INSTANCE.toIngredient(ingredientDTO);
        return ingredientService.update(ingredient.withId(id));
    }

    @Override
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a ingredient in the database.")
    public Mono<Void> delete(@PathVariable Integer id) {
        return ingredientService.delete(id);
    }

}
