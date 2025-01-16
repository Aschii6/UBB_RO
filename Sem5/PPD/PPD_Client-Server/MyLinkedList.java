package com.example;

import java.util.ArrayList;
import java.util.List;

public class MyLinkedList {
    private final MyNode _start;
    private final MyNode _final;

    private final List<String> disqualifiedParticipants = new ArrayList<>();

    public MyLinkedList() {
        _start = new MyNode("", "start", 0);
        _final = new MyNode("", "final", 0);

        _start.setNext(_final);
    }

    public void add(MyNode node) {
        if (node.getPoints() == 0)
            return;

        if (disqualifiedParticipants.contains(node.getId()))
            return;

        if (node.getPoints() == -1) {
            disqualifiedParticipants.add(node.getId());

            _start.lockNode();

            MyNode current = _start.getNext();
            current.lockNode();
            MyNode previous = _start;

            try {
                if (current == _final)
                    return;

                while (!current.equals(_final)) {
                    if (current.equals(node)) {
                        previous.setNext(current.getNext());
                        break;
                    }

                    previous.unlockNode();
                    previous = current;
                    current = current.getNext();
                    current.lockNode();
                }
            } finally {
                current.unlockNode();
                previous.unlockNode();
            }

            return;
        }

        _start.lockNode();

        MyNode current = _start.getNext();
        current.lockNode();
        MyNode previous = _start;

        try {
            if (current == _final) {
                _start.setNext(node);
                node.setNext(_final);
                return;
            }

            while (!current.equals(_final)) {
                if (current.equals(node)) {
                    current.setPoints(current.getPoints() + node.getPoints());
                    return;
                }

                previous.unlockNode();
                previous = current;
                current = current.getNext();
                current.lockNode();
            }

            // current is _final, previous is last elem before _final
            previous.setNext(node);
            node.setNext(current);
        } finally {
            current.unlockNode();
            previous.unlockNode();
        }
    }

    public List<MyNode> getResultsSorted() {
        List<MyNode> result = new ArrayList<>();

        MyNode current = _start.getNext();

        while (current != _final) {
            if (!disqualifiedParticipants.contains(current.getId()))
                result.add(current);

            current = current.getNext();
        }

        result.sort((o1, o2) -> o2.getPoints().compareTo(o1.getPoints()));

        return result;
    }
}
