package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnexpectedApiErrorException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.models.mvc.data.loginlog.LoginLogViewData;
import hr.algebra.gamearena.webapp.service.loginlog.ILogService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogMvcController {

    private static final String LOGIN_LOG_VIEW = "logs";

    private final ILogService logService;

    public LogMvcController(ILogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listLogs() throws UnauthorizedException, ForbiddenException {
        try {
            return MvcResponse.success(
                    HttpStatus.OK,
                    LOGIN_LOG_VIEW,
                    logService.getLoginLogs()
                            .stream()
                            .map(LoginLogViewData::from)
                            .toList()
            ).toModelAndView();
        } catch (NotFoundException | UnexpectedApiErrorException e) {
            return MvcResponse.errors(
                    e.getStatus(),
                    LOGIN_LOG_VIEW,
                    e.getMessages()
                            .stream()
                            .map(MvcError::new)
                            .toList()
            ).toModelAndView();
        }
    }
}
