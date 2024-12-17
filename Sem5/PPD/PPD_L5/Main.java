import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> countries = List.of("C1", "C2", "C3", "C4", "C5");
        int numberOfProblems = 10;
        int minParticipants = 80;
        int maxParticipants = 100;
        float unsolvedProbability = 0.1f;
        float fraudProbability = 0.02f;

        int readerThreads = 2;
        int workerThreads = 4;

        if (args.length >= 2) {
            readerThreads = Integer.parseInt(args[0]);
            workerThreads = Integer.parseInt(args[1]);
        }

        long startTime = System.currentTimeMillis();

        ParallelSolver parallelSolver = new ParallelSolver(countries, numberOfProblems, readerThreads, workerThreads);
        parallelSolver.solve();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(duration);
    }
}