package com.example.socialnetworkguitwo.service;

import com.example.socialnetworkguitwo.domain.User;
import com.example.socialnetworkguitwo.repo.UserRepository;
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
}
