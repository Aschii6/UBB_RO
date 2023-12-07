package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;
import java.util.List;

// de schimbat idTo la ceva cu lista de useri
// si la reply sa fie Message in loc de id
public class Message extends Entity<Long> {
    private Long idFrom, idTo;
    private String messageText;
    private LocalDateTime timeSent;
    private Long idMessageThisRepliedTo = 0L;

    public Message(Long idFrom, Long idTo, String messageText) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.messageText = messageText;
    }

    public void setIdMessageThisRepliedTo(Long idMessageThisRepliedTo) {
        this.idMessageThisRepliedTo = idMessageThisRepliedTo;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public String getMessageText() {
        return messageText;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public Long getIdMessageThisRepliedTo() {
        return idMessageThisRepliedTo;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    @Override
    public String toString() {
        return getMessageText();
    }
}
