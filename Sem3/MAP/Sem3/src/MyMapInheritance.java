import java.util.*;

public class MyMapInheritance extends TreeMap<Integer, List<Student>> {
    private static class GradeComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public MyMapInheritance(){
        super(new GradeComparator());
    }

    public void add(Student s){
        int grade = Math.round(s.getGrade());

        List<Student> foundList = get(grade);

        if (foundList != null){
            foundList.add(s);
        }
        else {
            ArrayList<Student> l = new ArrayList<>();
            l.add(s);
            put(grade, l);
        }
    }

    public Set<Map.Entry<Integer, List<Student>>> getEntries(){
        ArrayList<Map.Entry<Integer, List<Student>>> sortedStudents = new ArrayList<>(super.entrySet());
        Collections.sort(sortedStudents, new Comparator<Map.Entry<Integer, List<Student>>>() {
            @Override
            public int compare(Map.Entry<Integer, List<Student>> o1, Map.Entry<Integer, List<Student>> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        // Uhh nu sunt sigur daca e corect, de luat cod
        var set =  new TreeSet<>(new Comparator<Map.Entry<Integer, List<Student>>>() {
            @Override
            public int compare(Map.Entry<Integer, List<Student>> o1, Map.Entry<Integer, List<Student>> o2) {
                return o2.getKey().compareTo(o1.getKey());
            }
        });

        return set;
    }
}
