import java.util.ArrayList;
import java.util.List;

public class QueueContainer extends MyContainerSuperclass{
    @Override
    public Task remove() {
        if (tasks.isEmpty()){
            return null;
        }
        else {
            return tasks.removeLast();
        }
    }

    @Override
    public void add(Task task) {
        tasks.addFirst(task);
    }

    /*@Override
    public int size() {
        return tasks.size();
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }*/
}
