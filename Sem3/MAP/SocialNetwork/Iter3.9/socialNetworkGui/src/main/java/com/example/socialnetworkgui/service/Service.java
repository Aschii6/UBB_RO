package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.utils.events.ServiceChangeEvent;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class Service implements Observable<ServiceChangeEvent> {
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;

    private List<Observer<ServiceChangeEvent>> observers = new ArrayList<>();

    public Service(UserService userService, FriendshipService friendshipService, MessageService messageService){
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
    }

    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public void addUser(String firstName, String lastName, String tag) {
        userService.addUser(firstName, lastName, tag);
    }

    public void deleteUser(long id) {
        userService.deleteUser(id);
    }

    public void updateUser(String firstName, String lastName, long id){
        userService.updateUser(firstName, lastName, id);
    }

    public Iterable<Friendship> getAllFriendships(){
        return friendshipService.getAllFriendships();
    }

    public void addFriendship(long id1, long id2) {
        if (id1 == id2)
            throw new ServiceException("An user can't befriend himself.\n");

        userService.getUser(id1);
        userService.getUser(id2);

        friendshipService.addFriendship(id1, id2);
    }

    public void removeFriendship(long id1, long id2) {
        friendshipService.deleteFriendship(id1, id2);
    }

    public void updateFriendship(long id1, long id2, LocalDateTime localDateTime){
        friendshipService.updateFriendship(id1, id2, localDateTime);
    }

    public int noOfCommunities() {
        List<User> users = (List<User>) userService.getAllUsers();

        List<Friendship> friendships = (List<Friendship>) friendshipService.getAllFriendships();

        int nr = 0;
        while (!users.isEmpty()){
            nr++;
            DFS1(users.get(0), users, friendships);
        }

        return nr;
    }

    private void DFS1(User user, List<User> users, List<Friendship> friendships){
        users.remove(user);
        for (Friendship friendship : friendships) {
            if (friendship.getId().getLeft().equals(user.getId())){
                User friend = userService.getUser(friendship.getId().getRight());

                if (users.contains(friend))
                    DFS1(friend, users, friendships);
            }
            if (friendship.getId().getRight().equals(user.getId())){
                User friend = userService.getUser(friendship.getId().getLeft());

                if (users.contains(friend))
                    DFS1(friend, users, friendships);
            }
        }
    }

    public Optional<Iterable<User>> biggestCommunity() {
        return Optional.empty();
    }

    public Iterable<FriendDTO> allFriendsOfAUserFromAMonth(long id, int year, int month) {
        userService.getUser(id);

        List<Friendship> friendships = (List<Friendship>) friendshipService.getAllFriendships();

        Predicate<Friendship> friendOnRight = f -> f.getId().getLeft() == id;
        Predicate<Friendship> friendOnLeft = f -> f.getId().getRight() == id;

        Predicate<Friendship> byYear = f -> f.getDate().getYear() == year;
        Predicate<Friendship> byMonth = f -> f.getDate().getMonthValue() == month;

        List<FriendDTO> rez = new ArrayList<>();

        rez.addAll(friendships.stream().filter(friendOnRight.and(byYear).and(byMonth))
                .map(f -> new FriendDTO(f.getId().getRight(), userService.getUser(f.getId().getRight()).getFirstName(),
                        userService.getUser(f.getId().getRight()).getLastName(), f.getDate()))
                .toList());

        rez.addAll(friendships.stream().filter(friendOnLeft.and(byYear).and(byMonth))
                .map(f -> new FriendDTO(f.getId().getLeft(), userService.getUser(f.getId().getLeft()).getFirstName(),
                        userService.getUser(f.getId().getLeft()).getLastName(), f.getDate()))
                .toList());

        return rez;
    }

    public Iterable<User> getAllFriendReqs(Long id) {
        List<Friendship> friendships = (List<Friendship>) friendshipService.getAllFriendships();

        Predicate<Friendship> friendOnLeft = f -> Objects.equals(f.getId().getRight(), id);
        Predicate<Friendship> friendshipPending = f -> f.getStatus() == Status.PENDING;

        return friendships.stream().filter(friendOnLeft.and(friendshipPending))
                .map(f -> userService.getUser(f.getId().getLeft())).toList();
    }

    public Iterable<User> getAllFriends(Long userId) {
        List<Friendship> friendships = (List<Friendship>) friendshipService.getAllFriendships();

        Predicate<Friendship> friendOnLeft = f -> Objects.equals(f.getId().getRight(), userId);
        Predicate<Friendship> friendOnRight = f -> Objects.equals(f.getId().getLeft(), userId);
        Predicate<Friendship> friendshipAccepted = f -> f.getStatus() == Status.APPROVED;

        List<User> friends = new ArrayList<>(friendships.stream().filter(friendOnLeft.and(friendshipAccepted))
                .map(f -> userService.getUser(f.getId().getLeft())).toList());

        friends.addAll(friendships.stream().filter(friendOnRight.and(friendshipAccepted))
                .map(f -> userService.getUser(f.getId().getRight())).toList());

        return friends;
    }

    public Iterable<Message> getAllMessagesBetweenTwoUsers(Long id1, Long id2) {
        return messageService.getAllMessagesBetweenTwoUsers(id1, id2);
    }

    public UserService getUserService() {
        return userService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    @Override
    public void notifyObservers(ServiceChangeEvent serviceChangeEvent) {
        observers.forEach(o -> o.update(serviceChangeEvent));
    }

    @Override
    public void addObserver(Observer<ServiceChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<ServiceChangeEvent> observer) {
        observers.remove(observer);
    }

    public void sendFriendRequest(Long userId, String tag) {
        Long idTo = userService.getUserByTag(tag).getId();

        if (Objects.equals(userId, idTo))
            throw new ServiceException("An user can't befriend himself.");

        friendshipService.addFriendRequest(userId, idTo);

        notifyObservers(new ServiceChangeEvent());
    }

    public void acceptFriendRequest(Long userId, Long idAccepted) {
        friendshipService.acceptFriendRequest(userId, idAccepted);

        notifyObservers(new ServiceChangeEvent());
    }

    public void rejectFriendRequest(Long userId, Long idRejected) {
        friendshipService.rejectFriendRequest(userId, idRejected);

        notifyObservers(new ServiceChangeEvent());
    }

    public void addMessage(Long userId, Long idTo, String messageText, Long idMessageThisRepliedTo) {
        Optional<Friendship> opt = friendshipService.getFriendship(userId, idTo);

        if (opt.isEmpty())
            throw new ServiceException("Friendship doesn't exist");

        if (opt.get().getStatus() != Status.APPROVED)
            throw new ServiceException("Friendship not accepted");

        messageService.addMessage(userId, idTo, messageText, idMessageThisRepliedTo);

        notifyObservers(new ServiceChangeEvent());
    }

    public Message getMessage(Long id) {
        return messageService.getMessage(id);
    }

    public User getUserByTag(String tag) {
        return userService.getUserByTag(tag);
    }
}
