import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelSolver {
    private final List<String> countries;
    private final int numberOfProblems;

    private final int numOfReaderThreads;
    private final int numOfWorkerThreads;

    public ParallelSolver(List<String> countries, int numberOfProblems, int numOfReaderThreads, int numOfWorkerThreads) {
        this.countries = countries;
        this.numberOfProblems = numberOfProblems;
        this.numOfReaderThreads = numOfReaderThreads;
        this.numOfWorkerThreads = numOfWorkerThreads;
    }

    public void solve() {
        SyncedQueue queue = new SyncedQueue(countries.size() * numberOfProblems);
        MyLinkedList list = new MyLinkedList();

        ExecutorService executorService = Executors.newFixedThreadPool(numOfReaderThreads);

        for (String country : countries) {
            for (int i = 0; i < numberOfProblems; i++) {
                String filename = "C:/Users/Daniel/IdeaProjects/PPD_T5/src/inputs/Results" + country + "_" + "P" + (i + 1) + ".txt";

                executorService.submit(new ReaderTask(queue, filename));
            }
        }

        executorService.shutdown();

        WorkerThread[] workerThreads = new WorkerThread[numOfWorkerThreads];

        for (int i = 0; i < numOfWorkerThreads; i++) {
            workerThreads[i] = new WorkerThread(queue, list);
            workerThreads[i].start();
        }

        try {
            for (int i = 0; i < numOfWorkerThreads; i++) {
                workerThreads[i].join();
            }
        } catch (InterruptedException e) {
            System.out.println("Error joining threads: " + e.getMessage());
        }

        List<MyNode> results = list.getResultsSorted();

        try {
            FileWriter writer = new FileWriter("C:/Users/Daniel/IdeaProjects/PPD_T5/src/results/Result.txt");

            for (MyNode node : results) {
                writer.write(node.getId() + "," + node.getPoints() + "\n");
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        checkResults(results);
    }

    void checkResults(List<MyNode> parallelResults) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:/Users/Daniel/IdeaProjects/PPD_T5/src/results/SequentialResult.txt"));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String id = tokens[0];
                int points = Integer.parseInt(tokens[1]);

                MyNode node = new MyNode(id, points);

                for (MyNode parallelNode : parallelResults) {
                    if (parallelNode.getId().equals(node.getId())) {
                        if (!parallelNode.getPoints().equals(node.getPoints())) {
                            System.out.println("Results do not match");
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
