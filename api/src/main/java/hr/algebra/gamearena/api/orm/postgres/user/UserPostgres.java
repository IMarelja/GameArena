package hr.algebra.gamearena.api.orm.postgres.user;

import hr.algebra.gamearena.api.model.user.UserActiveStatusUpdate;
import hr.algebra.gamearena.api.model.user.UserSave;
import hr.algebra.gamearena.api.model.user.UserSelfDeleteUpdate;
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
    private user_role role;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public UserPostgres fromUserSave(
            UserSave userSave
    ){
        this.email = userSave.getEmail();
        this.username = userSave.getUsername();
        this.passwordHash = userSave.getPasswordHash();
        this.passwordSalt = userSave.getPasswordSalt();
        this.role = user_role.fromRole(userSave.getRole());
        this.isActive = true;
        this.isDeleted = false;
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }

    public UserPostgres fromUserSelfDeleteUpdate(UserSelfDeleteUpdate update) {
        this.username = update.getUsername();
        this.email = update.getEmail();
        this.passwordHash = update.getPasswordHash();
        this.passwordSalt = update.getPasswordSalt();
        this.isDeleted = true;
        return this;
    }

    public UserPostgres fromUserActiveStatusUpdate(UserActiveStatusUpdate update) {
        this.isActive = update.getIsActive();
        return this;
    }
}
