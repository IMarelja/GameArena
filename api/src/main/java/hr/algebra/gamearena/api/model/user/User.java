package hr.algebra.gamearena.api.model.user;

import hr.algebra.gamearena.api.orm.postgres.user.UserPostgres;

import java.time.LocalDateTime;

public record User(
        Long id,
        String username,
        String email,
        String passwordHash,
        String passwordSalt,
        Role role,
        Boolean isActive,
        Boolean isDeleted,
        LocalDateTime createdAt
    ) {

    public static User fromPostgres(UserPostgres userPostgres) {
        return new User(
                userPostgres.getId(),
                userPostgres.getUsername(),
                userPostgres.getEmail(),
                userPostgres.getPasswordHash(),
                userPostgres.getPasswordSalt(),
                Role.fromRolePostgres(userPostgres.getRole()),
                userPostgres.getIsActive(),
                userPostgres.getIsDeleted(),
                userPostgres.getCreatedAt().toLocalDateTime()
        );
    }

    public static User deletedUser() {
        return new User(-1L, "[DELETED]", null, null, null, Role.USER, false, true, null);
    }
}

