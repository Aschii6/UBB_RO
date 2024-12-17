import java.io.BufferedReader;
import java.io.FileReader;

public class ReaderTask implements Runnable {
    private final SyncedQueue queue;
    private final String filename;

    public ReaderTask(SyncedQueue queue, String filename) {
        this.queue = queue;
        this.filename = filename;
    }

    @Override
    public void run() {
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

            queue.readingTaskFinished();
            reader.close();
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
