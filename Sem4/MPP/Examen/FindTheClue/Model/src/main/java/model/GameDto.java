package model;

import java.util.List;

public class GameDto extends Entity<Long> {
    private String clue;
    private Integer noOfTries;
    private List<Guess> guesses;

    public GameDto(String clue, Integer noOfTries, List<Guess> guesses) {
        this.clue = clue;
        this.noOfTries = noOfTries;
        this.guesses = guesses;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public Integer getNoOfTries() {
        return noOfTries;
    }

    public void setNoOfTries(Integer noOfTries) {
        this.noOfTries = noOfTries;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }
}
