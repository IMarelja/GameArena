package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.exceptions.extenders.ForbiddenException;
import hr.algebra.gamearena.webapp.exceptions.extenders.NotFoundException;
import hr.algebra.gamearena.webapp.exceptions.extenders.UnauthorizedException;
import hr.algebra.gamearena.webapp.models.mvc.MvcError;
import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.service.loginlog.ILogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogMvcController {

    private final ILogService logService;

    public LogMvcController(ILogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView listLogs() throws UnauthorizedException, ForbiddenException {
        try {
            return MvcResponse.fromApiResult("logs", logService.getLoginLogs(), data -> data).toModelAndView();
        } catch (NotFoundException e) {
            return MvcResponse.errors(e.getStatus(), "logs", MvcError.fromListString(e.getMessages())).toModelAndView();
        }
    }
}
