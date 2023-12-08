package com.example.socialnetworkguitwo.utils.observer;

import com.example.socialnetworkguitwo.utils.events.Event;

public interface Observable<E extends Event> {
    void notifyObservers(E e);

    void addObserver(Observer<E> observer);

    void removeObserver(Observer<E> observer);
}
