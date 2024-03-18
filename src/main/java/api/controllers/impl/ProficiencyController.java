package api.controllers.impl;

<<<<<<< HEAD:src/main/java/api/controllers/impl/ProficiencyController.java
import api.annotations.PageableAsQueryParam;
import api.controllers.IController;
import api.mappers.ProficiencyMapper;
import api.models.dtos.ProficiencyDTO;
import api.models.entities.Proficiency;
=======
import api.domains.Proficiency;
import api.domains.dtos.ProficiencyDTO;
>>>>>>> main:src/main/java/api/controllers/ProficiencyController.java
import api.services.impl.ProficiencyService;
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
@RequestMapping("/proficiencies")
@Tag(name = "Proficiency")
public class ProficiencyController implements IController<Proficiency, ProficiencyDTO> {

    private final ProficiencyService proficiencyService;

    @Override
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a proficiency by ID.")
<<<<<<< HEAD:src/main/java/api/controllers/impl/ProficiencyController.java
    public Mono<Proficiency> findById(@PathVariable Integer id) {
=======
    public Mono<Proficiency> findById(@PathVariable Long id) {
>>>>>>> main:src/main/java/api/controllers/ProficiencyController.java
        return proficiencyService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a proficiency by name.")
    public Mono<Proficiency> findByName(@RequestParam String name) {
        return proficiencyService.findByName(name);
    }

    @GetMapping("/all")
    @PageableAsQueryParam
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all proficiencies.")
    public Mono<Page<Proficiency>> listAll(@Parameter(hidden = true) Pageable pageable) {
        return proficiencyService.findAll(pageable);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a proficiency in the database.")
    public Mono<Proficiency> save(@RequestBody @Valid ProficiencyDTO proficiencyDTO) {
        return proficiencyService.save(proficiencyDTO);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a proficiency in the database.")
<<<<<<< HEAD:src/main/java/api/controllers/impl/ProficiencyController.java
    public Mono<Void> update(@PathVariable Integer id, @RequestBody @Valid ProficiencyDTO proficiencyDTO) {
        Proficiency proficiency = ProficiencyMapper.INSTANCE.toProficiency(proficiencyDTO);
        return proficiencyService.update(proficiency.withId(id));
=======
    public Mono<Void> update(@RequestBody @Valid ProficiencyDTO proficiencyDTO, @PathVariable Long id) {
        return proficiencyService.update(proficiencyDTO, id);
>>>>>>> main:src/main/java/api/controllers/ProficiencyController.java
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a proficiency in the database.")
<<<<<<< HEAD:src/main/java/api/controllers/impl/ProficiencyController.java
    public Mono<Void> delete(@PathVariable Integer id) {
=======
    public Mono<Void> delete(@PathVariable Long id) {
>>>>>>> main:src/main/java/api/controllers/ProficiencyController.java
        return proficiencyService.delete(id);
    }

}

