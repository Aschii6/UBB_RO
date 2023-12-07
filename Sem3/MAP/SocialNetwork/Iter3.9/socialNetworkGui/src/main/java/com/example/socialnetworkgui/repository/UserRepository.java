package com.example.socialnetworkgui.repository;

import com.example.socialnetworkgui.domain.User;

import java.util.Optional;

public interface UserRepository extends Repository<Long, User> {
    Optional<User> findOneByTag(String tag);
}
