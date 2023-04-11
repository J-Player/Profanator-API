package api.domains.dtos;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.context.annotation.Profile;

@Profile("prod")
public record UserDTO (

        @NotEmpty(message = "The 'username' cannot be empty or null")
        String username,

        @NotEmpty(message = "The 'password' cannot be empty or null")
        String password,

        @NotEmpty(message = "The 'authorities' cannot be empty or null")
        String authorities,

        boolean accountNonLocked,

        boolean accountNonExpired,

        boolean credentialsNonExpired,

        boolean enabled

) {}
