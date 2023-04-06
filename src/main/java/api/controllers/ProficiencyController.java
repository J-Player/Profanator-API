package api.controllers;

import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
import api.mappers.ProficiencyMapper;
import api.services.impl.ProficiencyService;
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
@RequestMapping("/proficiencies")
@Tag(name = "Proficiency")
@SecurityScheme(name = "Basic Authentication", type = SecuritySchemeType.HTTP, scheme = "basic")
@SecurityRequirement(name = "Basic Authentication")
public class ProficiencyController implements IController<Proficiency, ProficiencyDTO> {

    private final ProficiencyService proficiencyService;

    @Override
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a proficiency by ID.")
    public Mono<Proficiency> findById(@PathVariable Long id) {
        return proficiencyService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a proficiency by name.")
    public Mono<Proficiency> findByName(@RequestParam String name) {
        return proficiencyService.findByName(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all proficiencies.")
    public Flux<Proficiency> listAll() {
        return proficiencyService.findAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a proficiency in the database.")
    public Mono<Proficiency> save(@RequestBody @Valid ProficiencyDTO proficiencyDTO) {
        Proficiency proficiency = ProficiencyMapper.INSTANCE.toProficiency(proficiencyDTO);
        return proficiencyService.save(proficiency);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a proficiency in the database.")
    public Mono<Void> update(@PathVariable Long id, @RequestBody @Valid ProficiencyDTO proficiencyDTO) {
        Proficiency proficiency = ProficiencyMapper.INSTANCE.toProficiency(proficiencyDTO);
        return proficiencyService.update(proficiency.withId(id));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a proficiency in the database.")
    public Mono<Void> delete(@PathVariable Long id) {
        return proficiencyService.delete(id);
    }

}

