package hr.algebra.gamearena.api.model.user;

import hr.algebra.gamearena.api.orm.postgres.user.UserRolePostgres;

public enum Role {
    USER,
    ADMIN;

    public static Role fromRolePostgres(UserRolePostgres role) {
        return switch (role) {
            case USER -> USER;
            case ADMIN -> ADMIN;
        };
    }
}
