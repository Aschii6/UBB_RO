package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.domain.validators.UserValidator;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.repository.UserRepository;
import com.example.socialnetworkgui.utils.events.UserChangeEvent;
import com.example.socialnetworkgui.utils.events.UserChangeEventType;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserService implements Observable<UserChangeEvent> {
    private UserRepository userRepo;
    private UserValidator userValidator;

    private List<Observer<UserChangeEvent>> observers = new ArrayList<>();

    public UserService(UserRepository userRepo, UserValidator userValidator) {
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

    public User getUserByTag(String tag) {
        Optional<User> opt = userRepo.findOneByTag(tag);

        if (opt.isPresent())
            return opt.get();

        throw new ServiceException("User with given tag doesn't exist.\n");
    }

    public void addUser(String firstName, String lastName, String tag) {
        User user = new User(firstName, lastName, tag);

        user.setId(0L);

        userValidator.validate(user);

        Optional<User> opt = userRepo.save(user);

        if (opt.isPresent())
            throw new ServiceException("Duplicate id.\n");

        notifyObservers(new UserChangeEvent(UserChangeEventType.ADD, null, user));
    }

    public void deleteUser(long id) {
        Optional<User> opt = userRepo.delete(id);

        if (opt.isEmpty())
            throw new ServiceException("User doesn't exist.\n");
        else {
            notifyObservers(new UserChangeEvent(UserChangeEventType.DELETE, opt.get(), null));
        }
    }

    public void updateUser(String firstName, String lastName, long id) {
        User user = new User(firstName, lastName, "");
        user.setId(id);

        userValidator.validate(user);

        Optional<User> optOld = userRepo.findOne(id);

        if (optOld.isPresent()) {
            Optional<User> opt = userRepo.update(user);

            if (opt.isEmpty())
                notifyObservers(new UserChangeEvent(UserChangeEventType.UPDATE, optOld.get(), user));
        }
        else
            throw new ServiceException("User doesn't exist.\n");
    }

    @Override
    public void notifyObservers(UserChangeEvent userChangeEvent) {
        observers.forEach(o -> o.update(userChangeEvent));
    }

    @Override
    public void addObserver(Observer<UserChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> observer) {
        observers.remove(observer);
    }
}

