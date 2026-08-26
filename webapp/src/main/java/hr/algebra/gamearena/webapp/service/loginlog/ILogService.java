package hr.algebra.gamearena.webapp.service.loginlog;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.cereal.loginog.LoginLogDecereal;
import hr.algebra.gamearena.webapp.models.service.ApiResult;

import java.util.List;

public interface ILogService {
    ApiResult<List<LoginLogDecereal>> getLoginLogs() throws UnauthorizedException, ForbiddenException, NotFoundException;
}
