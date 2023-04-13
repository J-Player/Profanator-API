package api.domains.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.context.annotation.Profile;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Profile("prod")
public class UserDTO {

        @NotEmpty(message = "The 'username' cannot be empty or null")
        private String username;

        @NotEmpty(message = "The 'password' cannot be empty or null")
        private String password;

        @NotEmpty(message = "The 'authorities' cannot be empty or null")
        private String authorities;

        private boolean accountNonLocked;

        private boolean accountNonExpired;

        private boolean credentialsNonExpired;

        private boolean enabled;

}
