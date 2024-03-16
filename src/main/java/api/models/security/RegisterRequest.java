package api.models.security;

import api.models.enums.UserRole;
import jakarta.validation.constraints.NotEmpty;

public record RegisterRequest(
        @NotEmpty(message = "The 'username' cannot be empty or null")
        String username,
        @NotEmpty(message = "The 'password' cannot be empty or null")
        String password,
        UserRole role) {

    public RegisterRequest(
            @NotEmpty(message = "The 'username' cannot be empty or null") String username,
            @NotEmpty(message = "The 'password' cannot be empty or null") String password) {
        this(username, password, null);
    }

}
