package hr.algebra.gamearena.api.service.user;

import hr.algebra.gamearena.api.dto.user.UserView;

import java.util.List;

public interface IUserService {
    List<UserView> findAll();
}
