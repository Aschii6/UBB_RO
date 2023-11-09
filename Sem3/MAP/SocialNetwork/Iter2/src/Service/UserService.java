package Service;

import Domain.Friendship;
import Domain.User;
import Domain.Validators.UserValidator;
import Repo.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserService {
    Repository<Long, User> userRepo;
    UserValidator userValidator;

    public UserService(Repository<Long, User> userRepo, UserValidator userValidator) {
        this.userRepo = userRepo;
        this.userValidator = userValidator;
    }

    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUser(Long id) {
        Optional<User> opt = userRepo.findOne(id);

        if (opt.isPresent())
            return opt.get();

        throw new ServiceException("User with given id doesn't exist.\n");
    }

    public void addUser(String firstName, String lastName, long id) {
        User user = new User(firstName, lastName);

        user.setId(id);

        userValidator.validate(user);

        Optional<User> opt = userRepo.save(user);

        if (opt.isPresent())
            throw new ServiceException("Duplicate id.\n");
    }

    public void deleteUser(long id) {
        Optional<User> opt = userRepo.delete(id);

        if (opt.isEmpty())
            throw new ServiceException("User doesn't exist.\n");
        else {
            User entity = opt.get();
        }
    }

    public void updateUser(String firstName, String lastName, long id) {
        User user = new User(firstName, lastName);
        user.setId(id);

        userValidator.validate(user);

        if (userRepo.update(user).isPresent())
            throw new ServiceException("User doesn't exist.\n");
    }
}
