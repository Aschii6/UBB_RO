import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class ReaderThread extends Thread {
    private final SyncedQueue queue;
    private final List<String> countries;
    private final int numberOfProblems;

    public ReaderThread(SyncedQueue queue, List<String> countries, int numberOfProblems) {
        this.queue = queue;
        this.countries = countries;
        this.numberOfProblems = numberOfProblems;
    }

    public void run() {
        for (String country : countries) {
            for (int i = 0; i < numberOfProblems; i++) {
                String filename = "C:/Users/Daniel/IdeaProjects/PPD_T4/src/inputs/Results" + country + "_" + "P" + (i + 1) + ".txt";

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(filename));

                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] tokens = line.split(",");
                        String participantId = tokens[0].substring(1);
                        int points = Integer.parseInt(tokens[1].substring(0, tokens[1].length() - 1));

                        Input input = new Input(participantId, points);

                        queue.push(input);
                    }

                    queue.readerFinished();

                    reader.close();
                } catch (Exception e) {
                    System.out.println("Error reading file: " + e.getMessage());
                }
            }
        }
    }
}
