package Service;

import Domain.FriendDTO;
import Domain.Friendship;
import Domain.Tuple;
import Domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service {
    private UserService userService;

    private FriendshipService friendshipService;

    public Service(UserService userService, FriendshipService friendshipService){
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public void addUser(String firstName, String lastName, long id) {
        userService.addUser(firstName, lastName, id);
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
//        userService.addFriendship(id1, id2);
        if (id1 == id2)
            throw new ServiceException("An user can't befriend himself.\n");

        userService.getUser(id1);
        userService.getUser(id2);

        friendshipService.addFriendship(id1, id2);
    }

    public void removeFriendship(long id1, long id2) {
//        userService.removeFriendship(id1, id2);
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
            DFS1(users.getFirst(), users, friendships);
        }

        return nr;
    }

    private void DFS1(User user, List<User> users, List<Friendship> friendships){
        users.remove(user);
        for (Friendship friendship : friendships) {
            if (friendship.getId().getLeft().equals(user.getId())){
                User friend = userService.getUser(friendship.getId().getRight());

                if (users.contains(friend)){
                    DFS1(friend, users, friendships);
                }
            }
            if (friendship.getId().getRight().equals(user.getId())){
                User friend = userService.getUser(friendship.getId().getLeft());
            }
        }
    }

    public Optional<Iterable<User>> biggestCommunity() {
        return Optional.empty();
    }

    public Iterable<FriendDTO> allFriendsOfAUserFromAMonth(long id, int year, int month) {
        userService.getUser(id);

        List<Friendship> friendships = (List<Friendship>) friendshipService.getAllFriendships();

        Predicate<Friendship> friend1 = f -> f.getId().getLeft() == id;
        Predicate<Friendship> friend2 = f -> f.getId().getRight() == id;

        Predicate<Friendship> byYear = f -> f.getDate().getYear() == year;
        Predicate<Friendship> byMonth = f -> f.getDate().getMonthValue() == month;

        List<FriendDTO> rez = new ArrayList<>();

         rez.addAll(friendships.stream().filter(friend1.and(byYear).and(byMonth))
                .map(f -> new FriendDTO(userService.getUser(f.getId().getRight()).getFirstName(),
                        userService.getUser(f.getId().getRight()).getLastName(), f.getDate()))
                .toList());

        rez.addAll(friendships.stream().filter(friend2.and(byYear).and(byMonth))
                .map(f -> new FriendDTO(userService.getUser(f.getId().getLeft()).getFirstName(),
                        userService.getUser(f.getId().getLeft()).getLastName(), f.getDate()))
                .toList());

        return rez;
    }
}
