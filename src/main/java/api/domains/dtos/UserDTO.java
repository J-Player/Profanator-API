package api.domains.dtos;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @NotEmpty(message = "The 'username' cannot be empty or null")
    private String username;

    @NotEmpty(message = "The 'password' cannot be empty or null")
    private String password;

    @NotEmpty(message = "The 'authorities' cannot be empty or null")
    private String authorities;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Builder.Default
    private boolean enabled = true;

}
