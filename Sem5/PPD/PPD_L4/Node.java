public class Node {
    private final String id;
    private Integer points;

    private Node next;

    public Node(String id, Integer points) {
        this.id = id;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public Integer getPoints() {
        return points;
    }

    public Node getNext() {
        return next;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "(" + id + "," + points + ")";
    }
}
