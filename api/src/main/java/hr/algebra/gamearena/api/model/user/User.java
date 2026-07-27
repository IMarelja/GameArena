package hr.algebra.gamearena.api.model.user;

import hr.algebra.gamearena.api.orm.postgres.UserPostgres;

import java.time.LocalDateTime;

public record User(
        Long id,
        String username,
        String email,
        String passwordHash,
        String passwordSalt,
        Role role,
        Boolean isActive,
        LocalDateTime createdAt
    ) {

    public static User fromPostgres(UserPostgres userPostgres) {
        return new User(
                userPostgres.getId(),
                userPostgres.getUsername(),
                userPostgres.getEmail(),
                userPostgres.getPasswordHash(),
                userPostgres.getPasswordSalt(),
                userPostgres.getRole(),
                userPostgres.getIsActive(),
                userPostgres.getCreatedAt()
        );
    }
}

