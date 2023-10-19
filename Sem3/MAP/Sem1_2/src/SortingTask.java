import java.lang.reflect.Array;
import java.util.ArrayList;

public class SortingTask extends Task {
    ArrayList<Integer> list;

    AbstractSorter abstractSorter;

    public SortingTask(String id, String description, ArrayList<Integer> l, AbstractSorter abstractSorter) {
        super(id, description);
        list = l;
        this.abstractSorter = abstractSorter;
    }

    @Override
    public void execute() {
        abstractSorter.sort(list);
    }
}
