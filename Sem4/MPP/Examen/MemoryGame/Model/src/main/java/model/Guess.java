package model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Guess implements Serializable {
    private Integer first, second;

    public Guess() {
    }

    public Guess(Integer first, Integer second) {
        this.first = first;
        this.second = second;
    }

    public Integer getFirst() {
        return first;
    }

    public Integer getSecond() {
        return second;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Guess{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
