package hr.algebra.gamearena.webapp.controller.mvc;

import hr.algebra.gamearena.webapp.models.mvc.MvcResponse;
import hr.algebra.gamearena.webapp.service.user.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ModelAndView listUsers() {
        return MvcResponse.fromApiResult("users", userService.getAllUsers(), data -> data).toModelAndView();
    }
}
