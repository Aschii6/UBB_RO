package com.example.socialnetworkguitwo.service;

import com.example.socialnetworkguitwo.domain.User;
import com.example.socialnetworkguitwo.repo.UserRepository;
import com.example.socialnetworkguitwo.utils.Encryption;
import com.example.socialnetworkguitwo.utils.events.UserChangeEvent;
import com.example.socialnetworkguitwo.utils.observer.Observable;
import com.example.socialnetworkguitwo.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements Observable<UserChangeEvent> {
    private final List<Observer<UserChangeEvent>> observers = new ArrayList<>();
    private UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void notifyObservers(UserChangeEvent userChangeEvent) {
        observers.forEach(obs -> obs.update(userChangeEvent));
    }

    @Override
    public void addObserver(Observer<UserChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> observer) {
        observers.remove(observer);
    }

    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUser(Long id) {
        Optional<User> opt = userRepo.findOne(id);

        if (opt.isPresent())
            return opt.get();

        throw new ServiceException("User not found");
    }

    public User getUser(String tag) {
        Optional<User> opt = userRepo.findOne(tag);

        if (opt.isPresent())
            return opt.get();

        throw new ServiceException("User not found");
    }

    public String getHashedPassword(Long id) {
        Optional<String> opt = userRepo.getHashedPassword(id);

        if (opt.isPresent())
            return opt.get();

        throw new ServiceException("User not found");
    }

    public void addUser(String firstName, String lastName, String tag, String password) {
        User user = new User(firstName, lastName, tag);
        Optional<User> opt = userRepo.save(user);

        if (opt.isPresent())
            throw new ServiceException("Tag already taken");

        Optional<User> optUser = userRepo.findOne(tag);

        if (optUser.isEmpty())
            throw new ServiceException("Error creating account");

        Optional<String> optPassword = userRepo.saveHashedPassword(optUser.get().getId(), Encryption.hashPassword(password));

        if (optPassword.isPresent())
            throw new ServiceException("Error creating account");

        notifyObservers(new UserChangeEvent());
    }
}
