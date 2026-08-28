package hr.algebra.gamearena.webapp.service.authentication;

import hr.algebra.gamearena.webapp.exceptions.extenders.*;
import hr.algebra.gamearena.webapp.models.cereal.authentication.LoginCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.RegisterCereal;
import hr.algebra.gamearena.webapp.models.cereal.authentication.TokenDecereal;

public interface IAuthenticationService {
    TokenDecereal login(LoginCereal loginCereal)
            throws BadRequestedExceptions, NotFoundException, ForbiddenException, UnexpectedApiErrorException, UnauthorizedException;

    TokenDecereal register(RegisterCereal registerCereal)
            throws ConflictException, BadRequestedExceptions, NotFoundException, ForbiddenException, UnexpectedApiErrorException, UnauthorizedException;
}
