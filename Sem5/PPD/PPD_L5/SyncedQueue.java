import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class SyncedQueue {
    private final Queue<Input> queue = new LinkedList<>();
    private final int maxSize = 50;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    private final int readingTasks;
    private int readingTasksFinished = 0;

    public SyncedQueue(int readingTasks) {
        this.readingTasks = readingTasks;
    }

    public void push(Input input) {
        lock.lock();

        try {
            while (queue.size() == maxSize) {
                notFull.await();
            }

            queue.add(input);
            notEmpty.signal();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Error waiting: " + e.getMessage());
        } finally {
            /*System.out.println("Thread " + Thread.currentThread().getName() +
                    " is adding. Queue size: " + queue.size() +
                    ", Readers finished: " + readersFinished);*/
            lock.unlock();
        }
    }

    public void readingTaskFinished() {
        lock.lock();

        try {
            readingTasksFinished++;

            if (readingTasksFinished == readingTasks) {
                notEmpty.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public Input remove() {
        lock.lock();

        try {
            while (queue.isEmpty() && readingTasksFinished < readingTasks) {
                notEmpty.await();
            }

            if (queue.isEmpty() && readingTasksFinished == readingTasks) {
                return null;
            }

            Input input = queue.remove();
            notFull.signal();

            return input;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Error waiting: " + e.getMessage());
            return null;
        } finally {
            /*System.out.println("Thread " + Thread.currentThread().getName() +
                    " is removing. Queue size: " + queue.size() +
                    ", Readers finished: " + readersFinished);*/
            lock.unlock();
        }
    }
}
