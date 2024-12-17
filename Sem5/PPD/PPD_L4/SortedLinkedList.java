import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SortedLinkedList {
    private Node head;
    private final List<Node> disqualifiedParticipants;
    private final Lock lock;

    public SortedLinkedList() {
        head = null;
        disqualifiedParticipants = new ArrayList<>();
        lock = new ReentrantLock();
    }

    public void add(Node node) {
        lock.lock();

        try {
            if (node.getPoints() == 0)
                return;

            if (disqualifiedParticipants.contains(node))
                return;

            if (node.getPoints() == -1) {
                disqualifiedParticipants.add(node);

                Node current = head;
                Node previous = null;
                while (current != null) {
                    if (current.getId().equals(node.getId())) {
                        if (previous == null) {
                            head = current.getNext();
                        } else {
                            previous.setNext(current.getNext());
                        }
                        break;
                    }
                    previous = current;
                    current = current.getNext();
                }
                return;
            }

            if (head == null) {
                head = node;
                return;
            }

            Node current = head;
            Node previous = null;

            while (current != null) {
                if (current.getId().equals(node.getId())) {
                    current.setPoints(current.getPoints() + node.getPoints());

                    if (previous == null) {
                        head = current.getNext();
                    } else {
                        previous.setNext(current.getNext());
                    }

                    addAux(current);
                    return;
                }
                previous = current;
                current = current.getNext();
            }

            addAux(node);
        } finally {
            lock.unlock();
        }
    }

    // Should only be called for a node with id not in the list
    private void addAux(Node node) {
        Node current = head;
        Node previous = null;

        while (current != null && current.getPoints() > node.getPoints()) {
            previous = current;
            current = current.getNext();
        }

        if (previous == null) {
            node.setNext(head);
            head = node;
        } else {
            previous.setNext(node);
            node.setNext(current);
        }
    }

    public List<Node> getResults() {
        List<Node> participants = new ArrayList<>();

        Node current = head;
        while (current != null) {
            participants.add(current);
            current = current.getNext();
        }

        return participants;
    }
}
