package com.example.socialnetworkguitwo.service;

import com.example.socialnetworkguitwo.domain.Friendship;
import com.example.socialnetworkguitwo.domain.Status;
import com.example.socialnetworkguitwo.domain.Tuple;
import com.example.socialnetworkguitwo.repo.FriendshipRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class FriendshipService {
    private FriendshipRepository friendshipRepo;

    public FriendshipService(FriendshipRepository friendshipRepo) {
        this.friendshipRepo = friendshipRepo;
    }

    public Iterable<Friendship> getAllFriendReqs(Long userId) {
        return friendshipRepo.getAllFriendReqs(userId);
    }

    public Iterable<Friendship> getAllFriendships(Long userId) {
        return friendshipRepo.getAllFriendships(userId);
    }

    public void addFriendReq(Long idFrom, Long idTo) {
        Friendship friendship = new Friendship(new Tuple<>(idFrom, idTo));
        friendship.setStatus(Status.PENDING);

        friendshipRepo.save(friendship);
    }

    public void acceptFriendReq(Long idFrom, Long idTo) {
        Optional<Friendship> opt = friendshipRepo.findOne(new Tuple<>(idFrom, idTo));

        if (opt.isEmpty())
            throw new ServiceException("Friend req doesn't exist");

        Friendship friendship = opt.get();
        friendship.setStatus(Status.APPROVED);
        friendship.setTimeSent(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        friendshipRepo.update(friendship);
    }
}
