package hr.algebra.gamearena.api.orm.postgres.user;

import hr.algebra.gamearena.api.model.user.Role;
import hr.algebra.gamearena.api.model.user.UserSave;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
    private UserRolePostgres role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public UserPostgres fromUserSave(
            UserSave userSave
    ){
        this.email = userSave.getEmail();
        this.username = userSave.getUsername();
        this.passwordHash = userSave.getPasswordHash();
        this.passwordSalt = userSave.getPasswordSalt();
        this.role = UserRolePostgres.fromRole(userSave.getRole());
        this.isActive = true;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }
}
