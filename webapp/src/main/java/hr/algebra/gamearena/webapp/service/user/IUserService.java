package hr.algebra.gamearena.webapp.service.user;

import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public interface IUserService {
    ApiResult<List<UserViewDtoDecereal>> getAllUsers() throws NotFoundException;
}
