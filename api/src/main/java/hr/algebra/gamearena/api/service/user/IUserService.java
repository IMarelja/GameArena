package hr.algebra.gamearena.api.service.user;

import hr.algebra.gamearena.api.dto.user.UserViewDto;

import java.util.List;

public interface IUserService {
    List<UserViewDto> findAll();
}
