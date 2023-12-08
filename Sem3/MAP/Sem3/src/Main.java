import java.util.*;

public class Main {
    public static void main(String[] args) {
        Student s1 = new Student("Dan", 4.5f);
        Student s2 = new Student("Ana", 8.5f);
        Student s3 = new Student("Dan", 4.5f);

        // Ex 2
        Set<Student> students = new HashSet<>();

        students.add(s1);
        students.add(s2);
        students.add(s3);

        System.out.println(students);

        // Ex 4
//        Set<Student> studentsTree = new TreeSet<>();

        Set<Student> studentsTree = new TreeSet<>((o1, o2) -> o2.getName().compareTo(o1.getName()));

        studentsTree.add(s1);
        studentsTree.add(s2);
        studentsTree.add(s3);

        System.out.println(studentsTree);

        // Ex 5

        Map<String, Student> map = new HashMap<>();

        map.put(s1.getName(), s1);
        map.put(s2.getName(), s2);
        map.put(s3.getName(), s3);

        for (String key : map.keySet()){
            System.out.println(map.get(key));
        }

        for (Map.Entry<String, Student> entry : map.entrySet()){
            System.out.println(entry.getValue());
        }

        // II
        // Ex 5

        MyMap myMap = new MyMap();

//        MyMapInheritance myMap = new MyMapInheritance();

        myMap.add(s1);
        myMap.add(s2);
        myMap.add(s3);
        myMap.add(new Student("Hm", 9));

        for (Map.Entry<Integer, List<Student>> entry : myMap.getEntries()){
            List<Student> studentList = entry.getValue();

            Collections.sort(studentList);

            System.out.println(studentList);
        }
    }
}