package api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import api.domain.Ingredient;
import api.service.IngredientService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ingredients")
@SecurityScheme(
        name = "Basic Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class IngredientController implements AbstractControllerImpl<Ingredient, Integer> {

    private final IngredientService ingredientService;

    @GetMapping("/all/{item}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all ingredients by item's name.", tags = {"ingredient"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Flux<Ingredient> listAllByItem(@RequestParam String item) {
        return ingredientService.findAllByItem(item);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all ingredients.", tags = {"ingredient"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Flux<Ingredient> listAll() {
        return ingredientService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a ingredient by id.", tags = {"ingredient"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Ingredient> findById(Integer id) {
        return ingredientService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a ingredient in the database.", tags = {"ingredient"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Ingredient> save(@RequestBody @Valid Ingredient ingredient) {
        return ingredientService.save(ingredient);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a ingredient in the database.", tags = {"ingredient"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> update(@RequestBody @Valid Ingredient ingredient) {
        return ingredientService.update(ingredient);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a ingredient in the database.", tags = {"ingredient"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> delete(@PathVariable Integer id) {
        return ingredientService.delete(id);
    }

}
