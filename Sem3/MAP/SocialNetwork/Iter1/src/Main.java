import Domain.User;
import Domain.Validators.UserValidator;
import Repo.InMemoryRepository;
import Service.Service;
import UI.Console;

public class Main {
    public static void main(String[] args) {
        InMemoryRepository<Long, User> userRepo = new InMemoryRepository<>(new UserValidator());

        User u1=new User("Alex", "Pop");
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

        // u1 - u4 graf complet

        u1.getFriends().add(u2);
        u1.getFriends().add(u3);
        u1.getFriends().add(u4);

        u2.getFriends().add(u1);
        u2.getFriends().add(u3);
        u2.getFriends().add(u4);

        u3.getFriends().add(u1);
        u3.getFriends().add(u2);
        u3.getFriends().add(u4);

        u4.getFriends().add(u1);
        u4.getFriends().add(u2);
        u4.getFriends().add(u3);

        // u5 - u9: lant de 4

        u5.getFriends().add(u6);
        u6.getFriends().add(u5);

        u6.getFriends().add(u7);
        u7.getFriends().add(u6);

        u7.getFriends().add(u8);
        u8.getFriends().add(u9);

        u8.getFriends().add(u9);
        u9.getFriends().add(u8);

        userRepo.save(u1);
        userRepo.save(u2);
        userRepo.save(u3);
        userRepo.save(u4);
        userRepo.save(u5);
        userRepo.save(u6);
        userRepo.save(u7);
        userRepo.save(u8);
        userRepo.save(u9);

        Service service = new Service(userRepo);
        Console console = new Console(service);
        console.run();
    }
}