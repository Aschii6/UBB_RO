package com.example.sem7.observer;

public interface Observer<E extends Event> {
    void update(E e);
}
