package com.example.socialnetworkguitwo.repo;

import com.example.socialnetworkguitwo.domain.Message;
import com.example.socialnetworkguitwo.domain.MessageDTO;

public interface MessageRepository extends Repository<Long, Message> {
    Iterable<MessageDTO> getAllMessagesBetweenTwoUsers(Long idFrom, Long idTo);
}
