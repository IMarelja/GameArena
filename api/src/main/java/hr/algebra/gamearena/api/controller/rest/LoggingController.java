package hr.algebra.gamearena.api.controller.rest;

import hr.algebra.gamearena.api.dto.loginlog.LoginLogsFullView;
import hr.algebra.gamearena.api.dto.other.ApiResponse;
import hr.algebra.gamearena.api.service.loginlog.ILoginLoggingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/log")
public class LoggingController {
    private final ILoginLoggingService loginLoggingService;

    public LoggingController(ILoginLoggingService loginLoggingService) {
        this.loginLoggingService = loginLoggingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoginLogsFullView>>> getLoggedTournaments() {
        return ResponseEntity.ok(ApiResponse.success(loginLoggingService.getAll()));
    }
}
