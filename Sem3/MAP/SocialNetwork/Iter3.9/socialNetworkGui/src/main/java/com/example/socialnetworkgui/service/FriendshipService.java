package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.Status;
import com.example.socialnetworkgui.domain.Tuple;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.repository.FriendshipRepository;
import com.example.socialnetworkgui.repository.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

public class FriendshipService {
    private FriendshipRepository friendshipRepo;

    public FriendshipService(FriendshipRepository friendshipRepo){
        this.friendshipRepo = friendshipRepo;
    }

    public void addFriendship(Long id1, Long id2) {
        Friendship friendship = new Friendship(new Tuple<>(id1, id2));

        Optional<Friendship> opt = friendshipRepo.save(friendship);

        if (opt.isPresent())
            throw new ServiceException("Friendship already exists.\n");
    }

    public void deleteFriendship(Long id1, Long id2) {
        Optional<Friendship> opt = friendshipRepo.delete(new Tuple<>(id1, id2));

        if (opt.isEmpty())
            throw new ServiceException("Friendship doesn't exist.\n");
    }

    public Iterable<Friendship> getAllFriendships(){
        return friendshipRepo.findAll();
    }

    public Optional<Friendship> getFriendship(Long userId, Long idTo) {
        return friendshipRepo.findOne(new Tuple<>(userId, idTo));
    }

    public void updateFriendship(long id1, long id2, LocalDateTime localDateTime) {
        Friendship friendship = new Friendship(new Tuple<>(id1, id2));

        friendship.setDate(localDateTime);

        if (friendshipRepo.update(friendship).isPresent())
            throw new ServiceException("Friendship doesn't exist.\n");
    }

    public void addFriendRequest(Long id1, Long id2) {
        Friendship friendship = new Friendship(new Tuple<>(id1, id2));
        friendship.setStatus(Status.PENDING);

        Optional<Friendship> opt = friendshipRepo.save(friendship);

        if (opt.isPresent())
            throw new ServiceException("Friendship already exists or friend request already sent.");
    }

    public void acceptFriendRequest(Long userId, Long idAccepted) {
        Optional<Friendship> opt = friendshipRepo.findOne(new Tuple<>(idAccepted, userId));

        if (opt.isPresent()) {
            Friendship friendship = opt.get();
            friendship.setStatus(Status.APPROVED);
            friendship.setDate(LocalDateTime.now());

            friendshipRepo.update(friendship);
        }
    }

    public void rejectFriendRequest(Long userId, Long idRejected) {
        friendshipRepo.delete(new Tuple<>(idRejected, userId));
    }

    // Mmm am incercat ceva da m am dat balta
    public Iterable<User> getAllFriendReqs(Long id) {
        return null;
    }
}

