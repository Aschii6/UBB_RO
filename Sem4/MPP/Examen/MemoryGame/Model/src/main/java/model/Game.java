package model;

import jakarta.persistence.*;

import java.util.List;

@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "Games")
public class Game extends Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private Integer points;

    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "configuration_id")
    private Configuration configuration;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Guess> positions;

    public Game() {
    }

    public Game(Player player, Integer points, Integer duration, Configuration configuration, List<Guess> positions) {
        this.player = player;
        this.points = points;
        this.duration = duration;
        this.configuration = configuration;
        this.positions = positions;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public List<Guess> getPositions() {
        return positions;
    }

    public void setPositions(List<Guess> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", player=" + player +
                ", points=" + points +
                ", duration=" + duration +
                ", configuration=" + configuration +
                ", positions=" + positions +
                '}';
    }
}
