package hr.algebra.gamearena.api.service.user;

import hr.algebra.gamearena.api.dto.user.UserFullViewDto;
import hr.algebra.gamearena.api.dto.user.UserViewDto;
import hr.algebra.gamearena.api.model.user.User;
import hr.algebra.gamearena.api.repository.user.IUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements IUserService {

    private final IUserRepo userRepo;

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean isActiveById(Long id) {
        return userRepo.existsByIdAndIsActive(id);
    }

    @Override
    public List<UserViewDto> findAll() {
        log.info("UserService findAll(): Fetching all users from the database...");

        var users = this.userRepo.findAll()
                .stream()
                .map(UserViewDto::fromUser)
                .toList();

        log.info("UserService findAll(): All users have been fetched from the database.");
        return users;

    }

    @Override
    public Optional<UserViewDto> findById(Long id) {
        return this.userRepo.findById(id)
                .map(UserViewDto::fromUser);
    }

    @Override
    public Optional<UserFullViewDto> findFullInfoById(Long id) {
        return this.userRepo.findById(id)
                .map(UserFullViewDto::fromUser);
    }
}
