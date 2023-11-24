package com.example.socialnetworkgui.utils.events;


import com.example.socialnetworkgui.domain.Message;

public class MessageChangeEvent implements Event {
    private MessageChangeEventType type;
    private Message oldMessage;
    private Message newMessage;

    public MessageChangeEvent(MessageChangeEventType type, Message oldMessage, Message newMessage) {
        this.type = type;
        this.oldMessage = oldMessage;
        this.newMessage = newMessage;
    }

    public MessageChangeEventType getType() {
        return type;
    }

    public Message getOldMessage() {
        return oldMessage;
    }

    public Message getNewMessage() {
        return newMessage;
    }
}
