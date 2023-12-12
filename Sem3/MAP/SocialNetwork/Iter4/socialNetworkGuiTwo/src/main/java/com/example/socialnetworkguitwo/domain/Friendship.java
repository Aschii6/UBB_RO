package com.example.socialnetworkguitwo.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Friendship extends Entity<Tuple<Long, Long>> {
    LocalDateTime timeSent;
    Status status;

    public Friendship(Tuple<Long, Long> idsTuple){
        this.setId(idsTuple);
        status = Status.PENDING;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent.truncatedTo(ChronoUnit.SECONDS);
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
                "date=" + timeSent +
                ", id=" + id +
                '}';
    }
}
