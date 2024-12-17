package lexical_analysis;

public class HashNode<K, V> {
    public K key;
    public V value;
    public int position;
    public int hashCode;
    public HashNode<K, V> next;

    public HashNode(K key, V value, int position, int hashCode) {
        this.key = key;
        this.value = value;
        this.position = position;
        this.hashCode = hashCode;
    }
}