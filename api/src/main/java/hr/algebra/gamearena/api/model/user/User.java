package hr.algebra.gamearena.api.model.user;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class User {

    private Long id;
    private String username;
    private String email;
    // private String profilePicUrl
    private String passwordHash;
    private String passwordSalt;
    // private String role
    private Boolean isActive;
    private LocalDateTime createdAt;

}

