package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;

public class FriendDTO {
    String firstName;
    String lastName;
    LocalDateTime localDateTime;

    public FriendDTO(String firstName, String lastName, LocalDateTime localDateTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return  lastName +
                "|" + firstName +
                "|" + localDateTime.toLocalDate();
    }
}

