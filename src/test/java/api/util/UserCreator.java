package api.util;

import api.integration.constraint.IntegrationConstraints;
import api.mappers.UserMapper;
import api.models.entities.User;
import api.models.security.RegisterRequest;

public abstract class UserCreator {

    public static User admin() {
        return User.builder()
                .username(IntegrationConstraints.ADMIN_USER)
                .password(IntegrationConstraints.ADMIN_PASSWORD)
                .role(IntegrationConstraints.ADMIN_ROLE)
                .build();
    }

    public static User user() {
        return User.builder()
                .username(IntegrationConstraints.REGULAR_USER)
                .password(IntegrationConstraints.REGULAR_PASSWORD)
                .role(IntegrationConstraints.USER_ROLE)
                .build();
    }

    public static RegisterRequest registerRequest() {
        return UserMapper.INSTANCE.toRegisterRequest(user());
    }

    public static User invalidUser() {
        return user().withUsername(null);
    }

    public static User userToRead() {
        return user().withUsername(IntegrationConstraints.REGULAR_USER.concat("_to_read"));
    }

    public static User userToUpdate() {
        return user().withUsername(IntegrationConstraints.REGULAR_USER.concat("_to_update"));
    }

    public static User userToDelete() {
        return user().withUsername(IntegrationConstraints.REGULAR_USER.concat("_to_delete"));
    }

}
