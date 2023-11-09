package Service;

import Domain.Friendship;
import Domain.Tuple;
import Repo.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

public class FriendshipService {
    Repository<Tuple<Long, Long>, Friendship> friendshipRepo;

    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> friendshipRepo){
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


    public void updateFriendship(long id1, long id2, LocalDateTime localDateTime) {
        Friendship friendship = new Friendship(new Tuple<>(id1, id2));

        friendship.setDate(localDateTime);

        if (friendshipRepo.update(friendship).isPresent())
            throw new ServiceException("Friendship doesn't exist.\n");
    }
}
