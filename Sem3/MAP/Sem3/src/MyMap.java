import java.lang.reflect.Array;
import java.util.*;

public class MyMap {
    private static class GradeComparator implements Comparator<Integer>{

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }
//    private Map<Integer, List<Student>> studentMap = new TreeMap<>((o1, o2) -> o2 - o1);

    private Map<Integer, List<Student>> studentMap = new TreeMap<>(new GradeComparator());

    public void add(Student s){
        int grade = Math.round(s.getGrade());

        List<Student> foundList = studentMap.get(grade);

        if (foundList != null){
            foundList.add(s);
        }
        else {
            ArrayList<Student> l = new ArrayList<>();
            l.add(s);
            studentMap.put(grade, l);
        }
    }

    public Set<Map.Entry<Integer, List<Student>>> getEntries(){
        return studentMap.entrySet();
    }
}
