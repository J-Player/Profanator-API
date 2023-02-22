package api.controllers;

import api.domains.Ingredient;
import api.domains.dtos.IngredientDTO;
import api.mappers.IngredientMapper;
import api.services.impl.IngredientService;
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

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ingredients")
@Tag(name = "Ingredient")
@SecurityScheme(name = "Basic Authentication", type = SecuritySchemeType.HTTP, scheme = "basic")
@SecurityRequirement(name = "Basic Authentication")
public class IngredientController implements IController<Ingredient, IngredientDTO> {

    private final IngredientService ingredientService;

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a ingredient by id.")
    public Mono<Ingredient> findById(@PathVariable UUID id) {
        return ingredientService.findById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all ingredients.")
    public Flux<Ingredient> listAll(@RequestParam(required = false) String item) {
        return item != null ? ingredientService.findAllByProduct(item) : ingredientService.findAll();
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
    public Mono<Void> update(@PathVariable UUID id, @RequestBody @Valid IngredientDTO ingredientDTO) {
        Ingredient ingredient = IngredientMapper.INSTANCE.toIngredient(ingredientDTO);
        return ingredientService.update(ingredient.withId(id));
    }

    @Override
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a ingredient in the database.")
    public Mono<Void> delete(@PathVariable UUID id) {
        return ingredientService.delete(id);
    }

}
