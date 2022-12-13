package api.util;

import api.domains.User;
import api.domains.dtos.UserDTO;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public abstract class UserCreator {

    private static final UUID ID = randomUUID();
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String AUTHORITIES = "ROLE_USER";

    public static User user() {
        return User.builder()
                .id(ID)
                .username(USERNAME)
                .password(PASSWORD)
                .authorities(AUTHORITIES)
                .build();
    }

    public static UserDTO userDTO() {
        return UserDTO.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

}
