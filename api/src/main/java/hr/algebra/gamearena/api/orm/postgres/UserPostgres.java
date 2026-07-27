package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.dto.user.UserCreateDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
public class UserPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    // @Column(name = "profile_pic_url", unique = true)
    // private String profilePicUrl

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "password_salt", nullable = false)
    private String passwordSalt;

    // @Column(name = "role")
    // private String/Role role

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UserPostgres fromUserCreateDto(
            UserCreateDto userCreateDto
    ){
        this.email = userCreateDto.getEmail();
        this.username = userCreateDto.getUsername();
        this.passwordHash = userCreateDto.getPasswordHash();
        this.passwordSalt = userCreateDto.getPasswordSalt();
        return this;
    }
}
