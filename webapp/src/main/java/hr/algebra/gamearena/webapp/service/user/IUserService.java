package hr.algebra.gamearena.webapp.service.user;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.user.UserFullViewDtoDecereal;
import hr.algebra.gamearena.webapp.models.cereal.user.UserSuspendCereal;
import hr.algebra.gamearena.webapp.models.cereal.user.UserViewDtoDecereal;

import java.util.List;

public interface IUserService {
    List<UserViewDtoDecereal> getAllUsers() throws NotFoundException, UnexpectedApiErrorException;
    UserViewDtoDecereal getUserById(Long id) throws NotFoundException, UnexpectedApiErrorException;
    UserFullViewDtoDecereal getMe() throws NotFoundException, UnauthorizedException, UnexpectedApiErrorException;
    void deleteMyAccount() throws UnauthorizedException;
    UserFullViewDtoDecereal getUserFullById(Long id) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;
    UserFullViewDtoDecereal suspendAccount(UserSuspendCereal cereal) throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;
}
