import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Circle c1 = new Circle(10d);
        Circle c2 = new Circle(15d);

        List<Circle> circles = Arrays.asList(c1, c2);

        // Anonymous class
        printArea(circles, new Area<Circle>() {
            @Override
            public double compute(Circle circle) {
                return circle.getRadius() * circle.getRadius() * Math.PI;
            }
        });
        System.out.println();

        printArea(circles, circle -> circle.getRadius() * circle.getRadius() * Math.PI);
        System.out.println();

        Predicate<Circle> smallCirclesPredicate = c -> c.getRadius() < 12d;

        printList(circles, smallCirclesPredicate);
        System.out.println();

        Predicate<Circle> largeCirclesPredicate = smallCirclesPredicate.negate();

        printList(circles, largeCirclesPredicate);
        System.out.println();

        Predicate<Circle> evenRadiusPredicate = circle -> circle.getRadius() % 2 == 0;

        printList(circles, smallCirclesPredicate.and(evenRadiusPredicate));
        System.out.println();

//        Function<String, Integer> function1 = s -> Integer.valueOf(s);
        Function<String, Integer> function1 = Integer::valueOf;

        System.out.println(function1.apply("125"));
        System.out.println();

        Function<String, Integer> power = function1.andThen(i -> i * i);
        System.out.println(power.apply("12"));
        System.out.println();

        Supplier<LocalDateTime> dateTimeSupplier = LocalDateTime::now;

        System.out.println(dateTimeSupplier.get());
        System.out.println();

        Supplier<List<String>> emptyStringListSupplier = ArrayList::new;

        System.out.println(emptyStringListSupplier.get());
        System.out.println();

        Comparator<Integer> integerDescComparator = (x, y) -> y - x;

        List<Integer> integerList = Arrays.asList(1, 2, 5, 6, -2, 7, -4);

        integerList.sort(integerDescComparator);

        System.out.println(integerList);
        System.out.println();

        Comparator<Integer> integerAscComparator = integerDescComparator.reversed();
//        Comparator<Integer> integerAscComparator = Integer::compare;

        integerList.sort(integerAscComparator);

        System.out.println(integerList);
        System.out.println();

        // Streamuri

        List<Integer> newIntegerList = filterGeneric(integerList, integer -> integer % 2 == 0);

        System.out.println(newIntegerList);
        System.out.println();

        List<Integer> anotherIntegerList = filterSortGeneric(integerList, integer -> integer % 2 == 0, integerDescComparator);

        System.out.println(anotherIntegerList);
        System.out.println();
    }

    private static <E> void printArea(List<E> list, Area<E> a){
        for (E e : list){
            System.out.println(a.compute(e));
        }
    }

    private static <E> void printList(List<E> list, Predicate<E> predicate){
        for (E e : list){
            if (predicate.test(e))
                System.out.println(e);
        }
    }

    public static <E> List<E> filterGeneric(List<E> list, Predicate<E> predicate){
        return list.stream().filter(predicate).collect(Collectors.toList()); // direct to list
    }

    public static <E> List<E> filterSortGeneric(List<E> list, Predicate<E> p, Comparator<E> c) {
        return list.stream().filter(p).sorted(c).collect(Collectors.toList());
    }
}