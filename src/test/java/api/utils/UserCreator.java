package api.utils;

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

    public static User user() {
        return User.builder()
                .id(ID)
                .username(USERNAME)
                .password(PASSWORD)
                .authorities(AUTHORITIES)
                .build();
    }

    public static UserDTO userDTO() {
        return new UserDTO(USERNAME, PASSWORD, AUTHORITIES,
                ACCOUNT_NON_LOCKED,
                ACCOUNT_NON_EXPIRED,
                CREDENTIALS_NON_EXPIRED,
                ENABLED);
    }

}
