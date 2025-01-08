package model;

import java.time.LocalDateTime;

public class Ranking extends Entity<Long> {
    private String alias;
    private LocalDateTime startTime;
    private Integer noOfTries;
    private String clue;

    public Ranking(String alias, LocalDateTime startTime, Integer noOfTries, String clue) {
        this.alias = alias;
        this.startTime = startTime;
        this.noOfTries = noOfTries;
        this.clue = clue;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getNoOfTries() {
        return noOfTries;
    }

    public void setNoOfTries(Integer noOfTries) {
        this.noOfTries = noOfTries;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }
}
