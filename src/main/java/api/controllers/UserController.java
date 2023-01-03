package api.controllers;

import api.domains.User;
import api.domains.dtos.UserDTO;
import api.mappers.UserMapper;
import api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.UUID;

@Profile("!dev & !test")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Only admin.")
@SecurityScheme(name = "Basic Authentication", type = SecuritySchemeType.HTTP, scheme = "basic")
@SecurityRequirement(name = "Basic Authentication")
public class UserController implements AbstractController<User, UserDTO> {

    private final UserService userService;

    @Override
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a user by ID.")
    public Mono<User> findById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns a user by username.")
    public Mono<User> findByName(@RequestParam String username) {
        return userService.findByName(username);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Returns all users.")
    public Flux<User> listAll() {
        return userService.findAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Saves a user in the database.")
    public Mono<User> save(@RequestBody @Valid UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toUser(userDTO);
        return userService.save(user);
    }

    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Updates a user in the database.")
    public Mono<Void> update(@PathVariable UUID id, @RequestBody @Valid UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toUser(userDTO);
        return userService.update(user.withId(id));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletes a user in the database.")
    public Mono<Void> delete(@PathVariable UUID id) {
        return userService.delete(id);
    }

}
