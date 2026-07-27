package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.user.Role;
import hr.algebra.gamearena.api.model.user.UserCreate;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@DynamicInsert
public class UserPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "password_salt", nullable = false)
    private String passwordSalt;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role", nullable = false, columnDefinition = "user_role")
    private Role role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public UserPostgres fromUserCreate(
            UserCreate userCreate
    ){
        this.email = userCreate.getEmail();
        this.username = userCreate.getUsername();
        this.passwordHash = userCreate.getPasswordHash();
        this.passwordSalt = userCreate.getPasswordSalt();
        this.role = userCreate.getRole();
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        return this;
    }
}
