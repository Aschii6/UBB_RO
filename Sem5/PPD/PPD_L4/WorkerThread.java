public class WorkerThread extends Thread {
    private final SyncedQueue queue;
    private final SortedLinkedList list;

    public WorkerThread(SyncedQueue queue, SortedLinkedList list) {
        this.queue = queue;
        this.list = list;
    }

    public void run() {
        while (true) {
            Input input = queue.remove();
            if (input == null) {
                return;
            }

            Node node = new Node(input.id(), input.points());
            list.add(node);
        }
    }
}
