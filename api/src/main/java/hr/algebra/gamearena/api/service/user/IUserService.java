package hr.algebra.gamearena.api.service.user;

import hr.algebra.gamearena.api.dto.user.UserFullViewDto;
import hr.algebra.gamearena.api.dto.user.UserViewDto;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    boolean isActiveById(Long id);
    List<UserViewDto> findAll();
    Optional<UserViewDto> findById(Long id);
    Optional<UserFullViewDto> findFullInfoById(Long id);
}
