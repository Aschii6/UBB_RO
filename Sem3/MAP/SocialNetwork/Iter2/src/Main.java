import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Domain.Validators.UserValidator;
import Repo.FriendshipDBRepository;
import Repo.InMemoryRepository;
import Repo.UserDBRepository;
import Service.Service;
import Service.UserService;
import Service.FriendshipService;
import UI.Console;

public class Main {
    public static void main(String[] args) {
        InMemoryRepository<Long, User> userRepo = new InMemoryRepository<>();

        InMemoryRepository<Tuple<Long, Long>, Friendship> friendshipRepo = new InMemoryRepository<>();

        /*User u1=new User("Alex", "Pop");
        User u2=new User("Dan", "Negru");
        User u3=new User("Radu", "Popescu");
        User u4=new User("Mihai", "Ardelean");
        User u5=new User("Adrian", "Cuza");
        User u6=new User("Paul", "Drobescu");
        User u7=new User("Ion", "Ionescu");
        User u8=new User("Marin", "Marinescu");
        User u9=new User("Poc", "Doc");

        u1.setId(1L);
        u2.setId(2L);
        u3.setId(3L);
        u4.setId(4L);
        u5.setId(5L);
        u6.setId(6L);
        u7.setId(7L);
        u8.setId(8L);
        u9.setId(9L);

        userRepo.save(u1);
        userRepo.save(u2);
        userRepo.save(u3);
        userRepo.save(u4);
        userRepo.save(u5);
        userRepo.save(u6);
        userRepo.save(u7);
        userRepo.save(u8);
        userRepo.save(u9);*/

        UserDBRepository userDBRepo = new UserDBRepository("jdbc:postgresql://localhost:5432/socialnetwork",
                "postgres", "parola");

        FriendshipDBRepository friendshipDBRepo = new FriendshipDBRepository("jdbc:postgresql://localhost:" +
                "5432/socialnetwork", "postgres", "parola");

        /*UserService userService = new UserService(userRepo, new UserValidator());

        FriendshipService friendshipService = new FriendshipService(friendshipRepo);*/

        UserService userService = new UserService(userDBRepo, new UserValidator());

        FriendshipService friendshipService = new FriendshipService(friendshipDBRepo);

        Service service = new Service(userService, friendshipService);

        Console console = new Console(service);
        console.run();
    }
}