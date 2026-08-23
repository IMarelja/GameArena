package hr.algebra.gamearena.api.dto.user;

import hr.algebra.gamearena.api.model.user.Role;

public enum RoleView {
    USER,
    ADMIN;

    public static RoleView fromRole(Role role) {
        return switch (role) {
            case USER -> USER;
            case ADMIN -> ADMIN;
        };
    }
}
