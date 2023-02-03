package api.domains.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@Profile("prod")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UserDTO {

    @NotEmpty(message = "The 'username' cannot be empty or null")
    private final String username;

    @NotEmpty(message = "The 'password' cannot be empty or null")
    private final String password;

    @NotEmpty(message = "The 'authorities' cannot be empty or null")
    private final String authorities;

    @Builder.Default
    private final boolean accountNonLocked = true;

    @Builder.Default
    private final boolean accountNonExpired = true;

    @Builder.Default
    private final boolean credentialsNonExpired = true;

    @Builder.Default
    private final boolean enabled = true;

}
