package com.example.socialnetworkguitwo.utils.observer;

import com.example.socialnetworkguitwo.utils.events.Event;

public interface Observer<E extends Event> {
    public void update(E e);
}
