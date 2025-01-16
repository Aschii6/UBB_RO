package com.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncedQueue {
    private final Queue<Input> queue = new LinkedList<>();
    private final int maxSize = 100;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();

    private int readingTasks = 0;
    private int readingTasksFinished = 0;

    public SyncedQueue() {
    }

    public boolean isFinished() {
        return readingTasksFinished == readingTasks && queue.isEmpty();
    }

    public synchronized void addReadingTask() {
        readingTasks++;
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
            lock.unlock();
        }
    }

    public synchronized void readingTaskFinished() {
        readingTasksFinished++;

        if (readingTasksFinished == readingTasks) {
            notEmpty.signalAll();
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
            lock.unlock();
        }
    }
}
