package hr.algebra.gamearena.api.orm.postgres.loginlog;

import hr.algebra.gamearena.api.model.loginlog.LoginLogSave;
import hr.algebra.gamearena.api.model.loginlog.LoginLogType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
    private login_log_type type;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public LoginLogPostgres fromLoginLogSave(LoginLogSave loginLogSave) {
        this.credential = loginLogSave.getCredential();
        this.ipv4 = loginLogSave.getIpv4();
        this.ipv6 = loginLogSave.getIpv6();
        this.type = login_log_type.fromLoginLogType(loginLogSave.getType());
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        return this;
    }

}
