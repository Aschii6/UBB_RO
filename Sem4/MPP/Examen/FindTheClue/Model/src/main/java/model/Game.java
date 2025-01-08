package model;

import java.time.LocalDateTime;
import java.util.List;

public class Game extends Entity<Long> {
    private Player player;
    private Configuration configuration;
    private List<Guess> guesses;
    private Integer noOfTries;
    private LocalDateTime startTime;

    public Game(Player player, Configuration configuration, List<Guess> guesses, Integer noOfTries,
                LocalDateTime startTime) {
        this.player = player;
        this.configuration = configuration;
        this.guesses = guesses;
        this.noOfTries = noOfTries;
        this.startTime = startTime;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }

    public void addGuess(Guess guess) {
        this.guesses.add(guess);
    }

    public Integer getNoOfTries() {
        return noOfTries;
    }

    public void setNoOfTries(Integer noOfTries) {
        this.noOfTries = noOfTries;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Game{" +
                "player=" + player +
                ", configuration=" + configuration +
                ", guesses=" + guesses +
                ", noOfTries=" + noOfTries +
                ", startTime=" + startTime +
                '}';
    }
}
