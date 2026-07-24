package hr.algebra.gamearena.api.model.user;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User {

    private final Long id;
    private final String username;
    private final String email;
    // private final String profilePicUrl
    private final String passwordHash;
    private final String passwordSalt;
    // private final String role
    private final Boolean isActive;
    private final LocalDateTime createdAt;

    public User(
            Long id,
            String username,
            String email,
            // String profilePicUrl
            String passwordHash,
            String passwordSalt,
            // String role
            Boolean isActive,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        // this.profilePicUrl = profilePicUrl
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        // this.role = role
        this.isActive = isActive;
        this.createdAt = LocalDateTime.now();
    }
}

