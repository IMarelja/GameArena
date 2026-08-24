package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home() {
        return MvcResponse.success(HttpStatus.OK, "home", null).toModelAndView();
    }
}
