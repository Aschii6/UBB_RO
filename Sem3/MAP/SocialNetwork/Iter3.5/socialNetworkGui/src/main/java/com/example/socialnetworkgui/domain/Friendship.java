package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Friendship extends Entity<Tuple<Long, Long>> {
    LocalDateTime date;
    Status status;

    public Friendship(Tuple<Long, Long> idsTuple){
        this.setId(idsTuple);
        status = Status.PENDING;

//        date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        // CAND O SA FIE CREATA, IN DB O SA PRIMEASCA DEFAULT TIME CHIAR DACA E PENDING SAU CUM O FI
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date.truncatedTo(ChronoUnit.SECONDS);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                ", id=" + id +
                '}';
    }
}
