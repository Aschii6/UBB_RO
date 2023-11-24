package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;

public class FriendDTO {
    Long id;
    String firstName;
    String lastName;
    LocalDateTime localDateTime;

    public FriendDTO(Long id, String firstName, String lastName, LocalDateTime localDateTime) {
        this.id = id;
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

