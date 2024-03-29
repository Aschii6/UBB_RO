package com.example.socialnetworkguitwo.repo;

import com.example.socialnetworkguitwo.domain.User;
import com.example.socialnetworkguitwo.utils.observer.Observable;

import java.util.Optional;

public interface UserRepository extends Repository<Long, User> {
    public Optional<User> findOne(String tag);
}
