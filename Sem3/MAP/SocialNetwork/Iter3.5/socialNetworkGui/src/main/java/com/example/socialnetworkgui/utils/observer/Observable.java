package com.example.socialnetworkgui.utils.observer;

import com.example.socialnetworkgui.utils.events.Event;

import java.util.ArrayList;
import java.util.List;

public interface Observable<E extends Event> {

    void notifyObservers(E e);

    void addObserver(Observer<E> observer);

    void removeObserver(Observer<E> observer);
}
