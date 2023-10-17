import java.util.ArrayList;
import java.util.Collections;

public class BubbleSorter extends AbstractSorter {
    @Override
    public void sort(ArrayList<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++){
            for (int j = i + 1; j < list.size(); j++){
                if (list.get(i) > list.get(j)){
                    Collections.swap(list, i, j);
                }
            }
        }
    }
}
