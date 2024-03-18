package api.utils;

<<<<<<< HEAD:src/test/java/api/util/UserCreator.java
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
=======
import api.domains.User;
import api.domains.dtos.UserDTO;

public abstract class UserCreator {

    private static final long ID = 1L;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String AUTHORITIES = "ROLE_USER";
    private static final boolean ACCOUNT_NON_LOCKED = true;
    private static final boolean ACCOUNT_NON_EXPIRED = true;
    private static final boolean CREDENTIALS_NON_EXPIRED = true;
    private static final boolean ENABLED = true;
>>>>>>> main:src/test/java/api/utils/UserCreator.java

    public static User user() {
        return User.builder()
                .username(IntegrationConstraints.REGULAR_USER)
                .password(IntegrationConstraints.REGULAR_PASSWORD)
                .role(IntegrationConstraints.USER_ROLE)
                .build();
    }

<<<<<<< HEAD:src/test/java/api/util/UserCreator.java
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
=======
    public static UserDTO userDTO() {
        return new UserDTO(USERNAME, PASSWORD, AUTHORITIES,
                ACCOUNT_NON_LOCKED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ENABLED);
>>>>>>> main:src/test/java/api/utils/UserCreator.java
    }

}
