package com.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyNode {
    private final String countryId;
    private final String id;
    private Integer points;

    private MyNode next;
    private final Lock lock = new ReentrantLock();

    public MyNode(String countryId, String id, Integer points) {
        this.countryId = countryId;
        this.id = id;
        this.points = points;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getId() {
        return id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public MyNode getNext() {
        return next;
    }

    public void setNext(MyNode next) {
        this.next = next;
    }

    public void lockNode() {
        lock.lock();
    }

    public void unlockNode() {
        lock.unlock();
    }

    @Override
    public String toString() {
        return "(" + id + "," + points + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MyNode myNode = (MyNode) obj;
        return id.equals(myNode.id);
    }
}
