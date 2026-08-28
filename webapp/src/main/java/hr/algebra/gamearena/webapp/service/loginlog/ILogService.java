package hr.algebra.gamearena.webapp.service.loginlog;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.cereal.loginog.LoginLogDecereal;

import java.util.List;

public interface ILogService {
    List<LoginLogDecereal> getLoginLogs() throws UnauthorizedException, ForbiddenException, NotFoundException, UnexpectedApiErrorException;
}
