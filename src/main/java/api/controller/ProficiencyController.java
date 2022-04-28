package api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import api.domain.Proficiency;
import api.service.ProficiencyService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/proficiencies")
@SecurityScheme(
        name = "Basic Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "basic")
public class ProficiencyController implements AbstractControllerImpl<Proficiency, Integer> {

    private final ProficiencyService proficiencyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a proficiency by name.", tags = {"proficiency"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Proficiency> findByName(@RequestParam String name) {
        return proficiencyService.findByName(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all proficiencies.", tags = {"proficiency"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Flux<Proficiency> listAll() {
        return proficiencyService.findAll();
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a proficiency by ID", tags = {"proficiency"}, security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Proficiency> findById(@PathVariable Integer id) {
        return proficiencyService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a proficiency in the database.", tags = {"proficiency"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Proficiency> save(@RequestBody @Valid Proficiency proficiency) {
        return proficiencyService.save(proficiency);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a proficiency in the database.", tags = {"proficiency"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> update(@RequestBody @Valid Proficiency proficiency) {
        return proficiencyService.update(proficiency);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a proficiency in the database.", tags = {"proficiency"}, security = @SecurityRequirement(name = "Basic Authentication"))
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> delete(@PathVariable Integer id) {
        return proficiencyService.delete(id);
    }

}
