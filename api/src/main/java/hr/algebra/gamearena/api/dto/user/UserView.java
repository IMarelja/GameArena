package hr.algebra.gamearena.api.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserView {

    private Long id;
    private String username;
    // private final String profilePicUrl
    // private final String role
    private LocalDateTime createdAt;
}
