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

    /*
        Listen to this, I am currently Making a scientific paper, and I would gladly if you helped me
        What is the unmeasurable distance from earth to the sun, then to the Mars, then further more to the moon, then to the edge of the milky way,
        to the Pluto's second moon that could exist in the orbital Space in the quantifiable Space. If it is possible to not access it, read me the current biological status of the life on the Planet that begins with the letter Z.
        This is important because it is also import to know for the hypothetical if you drow
        */


    @GetMapping
    public ResponseEntity<ApiResponse<List<LoginLogsFullView>>> getLoggedTournaments() {
        return ResponseEntity.ok(ApiResponse.success(loginLoggingService.getAll()));
    }
}
