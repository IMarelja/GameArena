package hr.algebra.gamearena.api.model.user;

import hr.algebra.gamearena.api.orm.postgres.UserPostgres;

import java.time.LocalDateTime;

/**
 * @param passwordHash private final String profilePicUrl
 * @param isActive     private final String role
 */
public record User(
        Long id,
        String username,
        String email,
        // String profilePicUrl
        String passwordHash,
        String passwordSalt,
        // Roles/String role
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
                userPostgres.getIsActive(),
                userPostgres.getCreatedAt()
        );
    }
}

