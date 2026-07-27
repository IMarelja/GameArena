package hr.algebra.gamearena.api.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserViewDto {

    private Long id;
    private String username;
    private LocalDateTime createdAt;
}
