import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final List<String> list = Arrays.asList("asf", "bcd", "asd", "bed", "bbb");

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*list.stream().filter(s -> {
            System.out.println("filter: " + s);
            return s.startsWith("b");
        }).map(s ->
        {
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).forEach(System.out::println);

        list.parallelStream().filter(s -> {
            System.out.println("thread: " + Thread.currentThread().getName());
            System.out.println("filter: " + s);
            return s.startsWith("b");
        }).map(s ->
        {
            System.out.println("thread: " + Thread.currentThread().getName());
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).forEachOrdered(s -> {
            System.out.println("thread: " + Thread.currentThread().getName());
            System.out.println("for each: " + s);
        });*/



        new ForkJoinPool(2).submit(() -> list.parallelStream().filter(s -> {
            System.out.println("thread: " + Thread.currentThread().getName());
            System.out.println("filter: " + s);
            return s.startsWith("b");
        }).map(s ->
        {
            System.out.println("thread: " + Thread.currentThread().getName());
            System.out.println("map: " + s);
            return s.toUpperCase();
        }).forEach(s -> {
            System.out.println("thread: " + Thread.currentThread().getName());
            System.out.println("for each: " + s);
        })
        ).get();

        System.out.println(list.stream().filter(s -> {
            return s.startsWith("b");
        }).map(s ->
        {
            return s.toUpperCase();
        }).reduce("", (a, b) -> a + b));

        Optional<String> x = list.stream().filter(s -> {
            return s.startsWith("b");
        }).map(s ->
        {
            return s.toUpperCase();
        }).reduce((a, b) -> a + b);

        System.out.println(x.isPresent());

        System.out.println(x.get());


    }
}