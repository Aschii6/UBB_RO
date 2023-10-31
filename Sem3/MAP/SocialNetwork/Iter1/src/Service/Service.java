package Service;

import Domain.User;
import Domain.Validators.UserValidator;
import Domain.Validators.Validator;
import Repo.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;

// Separat UserService si FriendshipService

public class Service {
    InMemoryRepository<Long, User> userRepo;
    private Validator<User> validator = new UserValidator();

    public Service(InMemoryRepository<Long, User> userRepo) {
        this.userRepo = userRepo;
    }

    public Iterable<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void addUser(String firstName, String lastName, long id) {
        User user = new User(firstName, lastName);
        if (id != 0)
            user.setId(id);

        validator.validate(user);
        userRepo.save(user);
    }

    public void deleteUser(long id) {
        User entity = userRepo.delete(id);

        for (User user : userRepo.findAll()) {
            user.getFriends().remove(entity);
        }
    }

    public void addFriendship(long id1, long id2) {
        User user1 = userRepo.findOne(id1);
        User user2 = userRepo.findOne(id2);

        List<User> l1 = user1.getFriends();
        List<User> l2 = user2.getFriends();

        if (l1.contains(user2) || l2.contains(user1)){
            throw new ServiceException("Friendship already exists.\n");
        }

        l1.add(user2);
        l2.add(user1);
    }

    public void removeFriendship(long id1, long id2) {
        User user1 = userRepo.findOne(id1);
        User user2 = userRepo.findOne(id2);

        boolean c1 = user1.getFriends().remove(user2);
        boolean c2 = user2.getFriends().remove(user1);

        if (!c1 || !c2)
            throw new ServiceException("Nonexistent friendship.\n");
    }

    public int noOfCommunities() {
        List<User> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);

        int nr = 0;

        while (!users.isEmpty()){
            DFS1(users.get(0), users);
            nr++;
        }

        return nr;
    }

    private void DFS1(User user, List<User> users){
        users.remove(user);
        for (User friend : user.getFriends()) {
            if (users.contains(friend))
                DFS1(friend, users);
        }
    }

    /*public Iterable<User> biggestCommunity() {
        List<User> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);

        List<User> biggestCommunity = new ArrayList<>();
        int max = 0;

        // lant de lungime maxima

        while (!users.isEmpty()){
            List<User> community = new ArrayList<>();
            DFS2(users.get(0), users, community);

            int aux = 0;
            for (User c : community) {
                aux += c.getFriends().size();
            }
            if (aux > max) {
                max = aux;
                biggestCommunity = community;
            }

            if (community.size() > max) {
                max = community.size();
                biggestCommunity = community;
            }
        }

        return max > 1 ? biggestCommunity : null;
    }*/

    public Iterable<User> biggestCommunity() {
        // Ceva DFS cu Discovered Time si Finished Time
        List<User> users = new ArrayList<>();

        userRepo.findAll().forEach(users::add);

        return null;
    }

    private void DFS2(User user, List<User> users, List<User> community){
        users.remove(user);
        community.add(user);
        for (User friend : user.getFriends()) {
            if (users.contains(friend))
                DFS2(friend, users, community);
        }
    }
}
