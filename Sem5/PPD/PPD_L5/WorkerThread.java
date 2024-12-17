public class WorkerThread extends Thread {
    private final SyncedQueue queue;
    private final MyLinkedList list;

    public WorkerThread(SyncedQueue queue, MyLinkedList list) {
        this.queue = queue;
        this.list = list;
    }

    public void run() {
        while (true) {
            Input input = queue.remove();
            if (input == null) {
                return;
            }

            MyNode node = new MyNode(input.id(), input.points());
            list.add(node);
        }
    }
}
