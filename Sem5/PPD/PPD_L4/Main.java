import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> countries = List.of("C1", "C2", "C3", "C4", "C5");
        int numberOfProblems = 10;
        int minParticipants = 80;
        int maxParticipants = 100;
        float unsolvedProbability = 0.1f;
        float fraudProbability = 0.02f;

        int threads = 4;
        int readerThreads = 1;

        if (args.length >= 2) {
            threads = Integer.parseInt(args[0]);
            readerThreads = Integer.parseInt(args[1]);
        }

        /*InputGenerator inputGenerator = new InputGenerator(countries, numberOfProblems, minParticipants,
                maxParticipants, unsolvedProbability, fraudProbability);

        inputGenerator.generate();*/

        long startTime = System.currentTimeMillis();

        if (threads == 1) {
            SequentialSolver sequentialSolver = new SequentialSolver(countries, numberOfProblems);
            sequentialSolver.solve();
        } else {
            ParallelSolver parallelSolver = new ParallelSolver(countries, numberOfProblems, threads, readerThreads);
            parallelSolver.solve();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println(duration);
    }
}