package hr.algebra.gamearena.api.service.user;

import hr.algebra.gamearena.api.dto.user.UserView;
import hr.algebra.gamearena.api.model.user.User;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final IUserRepo userRepo;

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserView> findAll() {
        return userRepo.findAll()
                .stream()
                .map(this::userToUserView)
                .toList();
    }

    private UserView userToUserView(User user) {
        UserView userView = new UserView();
        userView.setId(user.getId());
        userView.setUsername(user.getUsername());
        userView.setCreatedAt(user.getCreatedAt());
        return userView;
    }
}
