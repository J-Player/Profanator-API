package api.controller;

import api.domain.Ingredient;
import api.request.post.IngredientPostRequestBody;
import api.request.put.IngredientPutRequestBody;
import api.service.IngredientService;
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
@RequestMapping("/ingredients")
@SecurityScheme(name = "Basic Authentication", type = SecuritySchemeType.HTTP, scheme = "basic")
public class IngredientController implements Controller<Ingredient, IngredientPostRequestBody, IngredientPutRequestBody> {

    private final IngredientService ingredientService;

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a ingredient by id.", tags = {"ingredient"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Ingredient> findById(@PathVariable int id) {
        return ingredientService.findById(id);
    }

    @GetMapping("/{item}/{ingredient}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a ingredient by item's name and ingredient's name.", tags = {"ingredient"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Ingredient> findByItemAndIngredient(@PathVariable String item, @PathVariable String ingredient) {
        return ingredientService.findByProductAndName(item, ingredient);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all ingredients.", tags = {"ingredient"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Flux<Ingredient> listAll(@RequestParam(required = false) String item) {
        return item != null ? ingredientService.findAllByProduct(item) : ingredientService.findAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a ingredient in the database.", tags = {"ingredient"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Ingredient> save(@RequestBody @Valid IngredientPostRequestBody ingredient) {
        return ingredientService.save(ingredient);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a ingredient in the database.", tags = {"ingredient"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Void> update(@RequestBody @Valid IngredientPutRequestBody ingredient) {
        return ingredientService.update(ingredient);
    }

    @Override
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a ingredient in the database.", tags = {"ingredient"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Void> delete(@PathVariable int id) {
        return ingredientService.delete(id);
    }

}
