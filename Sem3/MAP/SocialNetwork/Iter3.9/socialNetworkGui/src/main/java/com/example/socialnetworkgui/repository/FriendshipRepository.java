package com.example.socialnetworkgui.repository;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.Tuple;

public interface FriendshipRepository extends Repository<Tuple<Long, Long>, Friendship> {
    public Iterable<Friendship> getAllFriendshipsOfAUser(Long id);
}
