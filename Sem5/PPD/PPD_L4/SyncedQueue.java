import java.util.LinkedList;
import java.util.Queue;

public class SyncedQueue {
    private final Queue<Input> queue = new LinkedList<>();
    private final int readers;
    private int readersFinished = 0;

    public SyncedQueue(int readers) {
        this.readers = readers;
    }

    public synchronized void push(Input input) {
        queue.add(input);
        notify();
    }

    public synchronized void readerFinished() {
        readersFinished++;

        if (readersFinished == readers) {
            notifyAll();
        }
    }

    public synchronized Input remove() {
        if (readersFinished == readers && queue.isEmpty()) {
            return null;
        }

        int numTries = 0;

        while (queue.isEmpty() && numTries < 1) {
            try {
                wait(20);
                numTries++;
            } catch (InterruptedException e) {
                System.out.println("Error waiting: " + e.getMessage());
                numTries++;
            }
        }

        if (queue.isEmpty()) {
            return null;
        }

        return queue.remove();
    }
}
