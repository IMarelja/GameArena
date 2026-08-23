package hr.algebra.gamearena.api.model.user;

import hr.algebra.gamearena.api.orm.postgres.user.user_role;

public enum Role {
    USER,
    ADMIN;

    public static Role fromRolePostgres(user_role role) {
        return switch (role) {
            case USER -> USER;
            case ADMIN -> ADMIN;
        };
    }
}
