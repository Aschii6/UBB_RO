package com.example.socialnetworkguitwo.repo;

import com.example.socialnetworkguitwo.domain.Friendship;
import com.example.socialnetworkguitwo.domain.Tuple;

public interface FriendshipRepository extends Repository<Tuple<Long, Long>, Friendship> {
    Iterable<Friendship> getAllFriendReqs(Long userId);

    Iterable<Friendship> getAllFriendships(Long userId);
}
