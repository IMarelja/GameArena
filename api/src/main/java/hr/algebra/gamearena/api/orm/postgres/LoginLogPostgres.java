package hr.algebra.gamearena.api.orm.postgres;

import hr.algebra.gamearena.api.model.loginlog.LoginLogSave;
import hr.algebra.gamearena.api.model.loginlog.LoginLogType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@Table(name = "login_logs")
public class LoginLogPostgres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "credential", nullable = false)
    private String credential;

    @Column(name = "ipv4")
    private String ipv4;

    @Column(name = "ipv6")
    private String ipv6;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", nullable = false, columnDefinition = "login_log_type")
    private LoginLogType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public LoginLogPostgres fromLoginLogSave(LoginLogSave loginLogSave) {
        this.credential = loginLogSave.getCredential();
        this.ipv4 = loginLogSave.getIpv4();
        this.ipv6 = loginLogSave.getIpv6();
        this.type = loginLogSave.getType();
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        return this;
    }

}
