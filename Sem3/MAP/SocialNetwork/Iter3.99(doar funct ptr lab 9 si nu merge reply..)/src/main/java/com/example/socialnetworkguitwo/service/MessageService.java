package com.example.socialnetworkguitwo.service;

import com.example.socialnetworkguitwo.domain.Message;
import com.example.socialnetworkguitwo.domain.MessageDTO;
import com.example.socialnetworkguitwo.domain.User;
import com.example.socialnetworkguitwo.repo.MessageRepository;

import java.util.List;
import java.util.Optional;

public class MessageService {
    private final MessageRepository messageRepo;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepo = messageRepository;
    }

    public Iterable<MessageDTO> getAllMessagesBetweenTwoUsers(Long idFrom, Long idTo) {
        return messageRepo.getAllMessagesBetweenTwoUsers(idFrom, idTo);
    }

    public void addMessage(Long idFrom, List<User> to, String messageText, Message repliedMessage) {
        Message message = new Message(idFrom, to, messageText, repliedMessage);

        Optional<Message> opt = messageRepo.save(message);

        if (opt.isPresent())
            throw new ServiceException("Nu s-a putut adauga mesajul");
    }

    public Message getMessage(Long idRepliedMessage) {
        Optional<Message> opt = messageRepo.findOne(idRepliedMessage);

        if (opt.isPresent())
            return opt.get();
        else
            throw new ServiceException("Message not found");
    }
}
