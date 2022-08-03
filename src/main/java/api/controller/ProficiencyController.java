package api.controller;

import api.domain.Proficiency;
import api.request.post.ProficiencyPostRequestBody;
import api.request.put.ProficiencyPutRequestBody;
import api.service.ProficiencyService;
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
@RequestMapping("/proficiencies")
@SecurityScheme(name = "Basic Authentication", type = SecuritySchemeType.HTTP, scheme = "basic")
public class ProficiencyController implements Controller<Proficiency, ProficiencyPostRequestBody, ProficiencyPutRequestBody> {

    private final ProficiencyService proficiencyService;

    @Override
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a proficiency by ID", tags = {"proficiency"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Proficiency> findById(@PathVariable int id) {
        return proficiencyService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a proficiency by name.", tags = {"proficiency"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Proficiency> findByName(@RequestParam String name) {
        return proficiencyService.findByName(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all proficiencies.", tags = {"proficiency"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Flux<Proficiency> listAll() {
        return proficiencyService.findAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a proficiency in the database.", tags = {"proficiency"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Proficiency> save(@RequestBody @Valid ProficiencyPostRequestBody proficiency) {
        return proficiencyService.save(proficiency);
    }

    @Override
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a proficiency in the database.", tags = {"proficiency"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Void> update(@RequestBody @Valid ProficiencyPutRequestBody proficiency) {
        return proficiencyService.update(proficiency);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a proficiency in the database.", tags = {"proficiency"},
            security = @SecurityRequirement(name = "Basic Authentication"))
    public Mono<Void> delete(@PathVariable int id) {
        return proficiencyService.delete(id);
    }

}
