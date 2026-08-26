package hr.algebra.gamearena.webapp.service.authentication;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.TokenDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

public interface IAuthenticationService {
    ApiResult<TokenDecereal> login(LoginCereal loginCereal)
            throws BadRequestedExceptions, NotFoundException, ForbiddenException, UnexpectedApiErrorException, UnauthorizedException;
}
