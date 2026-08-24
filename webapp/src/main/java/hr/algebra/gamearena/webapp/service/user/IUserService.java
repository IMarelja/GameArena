package hr.algebra.gamearena.webapp.service.user;

import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public interface IUserService {
    ApiResult<List<UserViewDtoDecereal>> getAllUsers();
}
