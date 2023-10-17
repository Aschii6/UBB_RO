import java.util.ArrayList;
import java.util.List;

public abstract class MyContainerSuperclass implements Container {
    protected List<Task> tasks = new ArrayList<>();

    public abstract Task remove();

    public abstract void add(Task task);

    @Override
    public int size() {
        return tasks.size();
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
