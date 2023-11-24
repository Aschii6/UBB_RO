package com.example.socialnetworkgui.service;

import com.example.socialnetworkgui.domain.Message;
import com.example.socialnetworkgui.repository.Repository;
import com.example.socialnetworkgui.utils.events.MessageChangeEvent;
import com.example.socialnetworkgui.utils.events.MessageChangeEventType;
import com.example.socialnetworkgui.utils.observer.Observable;
import com.example.socialnetworkgui.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageService implements Observable<MessageChangeEvent> {
    private Repository<Long, Message> messageRepo;

    private List<Observer<MessageChangeEvent>> observers = new ArrayList<>();

    public MessageService(Repository<Long, Message> messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Message getMessage(Long id) {
        Optional<Message> opt =  messageRepo.findOne(id);

        if (opt.isPresent())
            return opt.get();

        throw new ServiceException("Message with given id doesn't exist.");
    }

    public Iterable<Message> getAllMessages() {
        return messageRepo.findAll();
    }

    public void addMessage(Long idFrom, Long idTo, String messageText) {
        Message message = new Message(idFrom, idTo, messageText);

        // messageValidator.validate(message); ?

        messageRepo.save(message);

        notifyObservers(new MessageChangeEvent(MessageChangeEventType.ADD, null, message));
    }

    public void deleteMessage(Long id) {
        Optional<Message> opt = messageRepo.delete(id);

        if (opt.isEmpty())
            throw new ServiceException("Message doesn't exist.\n");
        else {
            notifyObservers(new MessageChangeEvent(MessageChangeEventType.DELETE, opt.get(), null));
        }
    }

    public void updateMessage(Long id, String messageText) {
        Optional<Message> oldOpt = messageRepo.findOne(id);

        if (oldOpt.isPresent()) {
            Message message = new Message(oldOpt.get().getIdFrom(), oldOpt.get().getIdTo(), messageText);
            message.setId(id);

            Optional<Message> opt = messageRepo.update(message);

            if (opt.isEmpty())
                notifyObservers(new MessageChangeEvent(MessageChangeEventType.UPDATE, oldOpt.get(), message));
        }
        else
            throw new ServiceException("Message doesn't exist.\n");
    }

    public Iterable<Message> getAllMessagesBetweenTwoUsers(Long fromId, Long toId) {

        return null;
    }

    @Override
    public void notifyObservers(MessageChangeEvent messageChangeEvent) {
        observers.forEach(o -> o.update(messageChangeEvent));
    }

    @Override
    public void addObserver(Observer<MessageChangeEvent> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> observer) {
        observers.remove(observer);
    }
}
