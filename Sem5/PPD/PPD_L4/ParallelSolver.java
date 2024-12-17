import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ParallelSolver {
    private final List<String> countries;
    private final int numberOfProblems;

    private final int numberOfThreads;
    private final int numberOfReaderThreads;

    public ParallelSolver(List<String> countries, int numberOfProblems, int numberOfThreads, int numberOfReaderThreads) {
        this.countries = countries;
        this.numberOfProblems = numberOfProblems;
        this.numberOfThreads = numberOfThreads;
        this.numberOfReaderThreads = numberOfReaderThreads;
    }

    public void solve() {
        SyncedQueue queue = new SyncedQueue(numberOfReaderThreads);
        SortedLinkedList list = new SortedLinkedList();

        ReaderThread[] readerThreads = new ReaderThread[numberOfReaderThreads];

        int numCountriesPerThread = countries.size() / numberOfReaderThreads;
        int rest = countries.size() % numberOfReaderThreads;

        int start = 0;
        for (int i = 0; i < numberOfReaderThreads; i++) {
            int end = start + numCountriesPerThread;
            if (rest > 0) {
                end++;
                rest--;
            }

            List<String> subCountries = countries.subList(start, end);
            readerThreads[i] = new ReaderThread(queue, subCountries, numberOfProblems);
            readerThreads[i].start();

            start = end;
        }

        int numberOfWorkerThreads = numberOfThreads - numberOfReaderThreads;
        WorkerThread[] workerThreads = new WorkerThread[numberOfWorkerThreads];

        for (int i = 0; i < numberOfWorkerThreads; i++) {
            workerThreads[i] = new WorkerThread(queue, list);
            workerThreads[i].start();
        }

        try {
            for (int i = 0; i < numberOfReaderThreads; i++) {
                readerThreads[i].join();
            }
        } catch (InterruptedException e) {
            System.out.println("Error joining threads: " + e.getMessage());
        }

        for (int i = 0; i < numberOfWorkerThreads; i++) {
            try {
                workerThreads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Error joining threads: " + e.getMessage());
            }
        }

        List<Node> results = list.getResults();

        try {
            FileWriter writer = new FileWriter("C:/Users/Daniel/IdeaProjects/PPD_T4/src/results/Result.txt");

            for (Node node : results) {
                writer.write(node.getId() + "," + node.getPoints() + "\n");
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        checkResults(results);
    }

    void checkResults(List<Node> parallelResults) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:/Users/Daniel/IdeaProjects/PPD_T4/src/results/SequentialResult.txt"));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String id = tokens[0];
                int points = Integer.parseInt(tokens[1]);

                Node node = new Node(id, points);

                for (Node parallelNode : parallelResults) {
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
