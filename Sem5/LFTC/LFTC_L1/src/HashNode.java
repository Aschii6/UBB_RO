public class HashNode<K, V> {
    K key;
    V value;
    int position;
    int hashCode;
    HashNode<K, V> next;

    public HashNode(K key, V value, int position, int hashCode) {
        this.key = key;
        this.value = value;
        this.position = position;
        this.hashCode = hashCode;
    }
}