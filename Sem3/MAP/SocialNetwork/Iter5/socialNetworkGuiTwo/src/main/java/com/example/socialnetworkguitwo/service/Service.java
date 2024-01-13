package com.example.socialnetworkguitwo.service;

import com.example.socialnetworkguitwo.domain.*;
import com.example.socialnetworkguitwo.repo.paging.Page;
import com.example.socialnetworkguitwo.repo.paging.Pageable;
import com.example.socialnetworkguitwo.utils.Encryption;
import com.example.socialnetworkguitwo.utils.events.ServiceEvent;
import com.example.socialnetworkguitwo.utils.observer.Observable;
import com.example.socialnetworkguitwo.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service implements Observable<ServiceEvent> {
    private final List<Observer<ServiceEvent>> observers = new ArrayList<>();

    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;

    public Service(UserService userService, FriendshipService friendshipService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
    }

    @Override
    public void notifyObservers(ServiceEvent serviceEvent) {
        observers.forEach(obs -> obs.update(serviceEvent));
    }

    @Override
    public void addObserver(Observer<ServiceEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<ServiceEvent> observer) {
        observers.remove(observer);
    }

    public UserService getUserService() {
        return userService;
    }

    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public Iterable<User> getAllUsersWithFriendReq (Long userId) {
        List<Friendship> friendReqs = (List<Friendship>) friendshipService.getAllFriendReqs(userId);

        return friendReqs.stream().map(friendship -> userService.getUser(friendship.getId().getLeft()))
                .collect(Collectors.toList());
    }

    public Iterable<User> getAllFriends(Long userId) {
        List<Friendship> friendships = (List<Friendship>) friendshipService.getAllFriendships(userId);

        Predicate<Friendship> friendOnLeft = f -> Objects.equals(f.getId().getRight(), userId);
        Predicate<Friendship> friendOnRight = f -> Objects.equals(f.getId().getLeft(), userId);

        List<User> friends = new ArrayList<>(friendships.stream().filter(friendOnLeft)
                .map(f -> userService.getUser(f.getId().getLeft())).toList());

        friends.addAll(friendships.stream().filter(friendOnRight)
                .map(f -> userService.getUser(f.getId().getRight())).toList());

        return friends;
    }

    public Page<User> getPagedFriendshipsOfAUser(Pageable pageable, Long userId, Status status) {
        Page<Friendship> page = friendshipService.getPagedFriendshipsOfAUser(pageable, userId, status);
        List<Friendship> friendships = (List<Friendship>) page.getElementsOnPage();

        Predicate<Friendship> friendOnLeft = f -> Objects.equals(f.getId().getRight(), userId);
        Predicate<Friendship> friendOnRight = f -> Objects.equals(f.getId().getLeft(), userId);

        List<User> friends = new ArrayList<>(friendships.stream().filter(friendOnLeft)
                .map(f -> userService.getUser(f.getId().getLeft())).toList());

        if (status != Status.PENDING)
            friends.addAll(friendships.stream().filter(friendOnRight)
                    .map(f -> userService.getUser(f.getId().getRight())).toList());

        return new Page<>(friends, page.getTotalNrOfElems());
    }

    public Iterable<Message> getAllMessagesBetweenTwoUsers(Long id1, Long id2) {
        List<MessageDTO> messageDTOS = (List<MessageDTO>) messageService.getAllMessagesBetweenTwoUsers(id1, id2);

        List<Message> messages = new ArrayList<>();
        for (MessageDTO messageDTO : messageDTOS) {
            List<User> to = new ArrayList<>();
            for (Long idTo : messageDTO.getTo()) {
                to.add(userService.getUser(idTo));
            }

            Message message = new Message(messageDTO.getIdFrom(), to, messageDTO.getMessageText(),
                    messageDTO.getRepliedMessage());
            message.setId(messageDTO.getId());
            message.setTimeSent(messageDTO.getTimeSent());

            messages.add(message);
        }
        return messages;
    }

    public void addFriendReq(Long idFrom, String tagTo) {
        Long idTo = userService.getUser(tagTo).getId();

        if (Objects.equals(idFrom, idTo))
            throw new ServiceException("An user can't befriend himself.");

        friendshipService.addFriendReq(idFrom, idTo);

        notifyObservers(new ServiceEvent());
    }

    public void acceptFriendReq(Long idFrom, Long idTo) {
        friendshipService.acceptFriendReq(idFrom, idTo);

        notifyObservers(new ServiceEvent());
    }

    public void addMessage(Long idFrom, List<User> to, String messageText, Message repliedMessage) {
        messageService.addMessage(idFrom, to, messageText, repliedMessage);

        notifyObservers(new ServiceEvent());
    }

    public void denyFriendReq(Long id1, Long id2) {
        friendshipService.deleteFriendship(new Tuple<>(id1, id2));

        notifyObservers(new ServiceEvent());
    }

    public User login(String tag, String password) {
        if (Encryption.verifyPassword(password, userService.getHashedPassword(userService.getUser(tag).getId())))
            return userService.getUser(tag);
        else
            throw new ServiceException("Wrong password.");
    }

    public User signup(String tag, String password, String firstName, String lastName) {
        userService.addUser(firstName, lastName, tag, password);

        return userService.getUser(tag);
    }
}
