package api.integration.constraint;

import api.models.enums.UserRole;

public abstract class IntegrationConstraints {

    public static final String ADMIN_USER = "admin";
    public static final String ADMIN_PASSWORD = "admin";
    public static final UserRole ADMIN_ROLE = UserRole.ADMIN;

    public static final String REGULAR_USER = "user";
    public static final String REGULAR_PASSWORD = "user";
    public static final UserRole USER_ROLE = UserRole.USER;

    public static final String PATH_PROFICIENCIES = "/proficiencies";
    public static final String PATH_ITEMS = "/items";
    public static final String PATH_INGREDIENTS = "/ingredients";
    public static final String PATH_USERS = "/users";

}
