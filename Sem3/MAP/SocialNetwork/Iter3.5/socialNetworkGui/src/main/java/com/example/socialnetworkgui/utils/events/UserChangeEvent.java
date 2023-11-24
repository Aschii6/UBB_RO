package com.example.socialnetworkgui.utils.events;

import com.example.socialnetworkgui.domain.User;

public class UserChangeEvent implements Event {
    private UserChangeEventType type;
    private User oldUser;
    private User newUser;

    public UserChangeEvent(UserChangeEventType type, User oldUser, User newUser) {
        this.type = type;
        this.oldUser = oldUser;
        this.newUser = newUser;
    }

    public UserChangeEventType getType() {
        return type;
    }

    public User getOldUser() {
        return oldUser;
    }

    public User getNewUser() {
        return newUser;
    }
}
