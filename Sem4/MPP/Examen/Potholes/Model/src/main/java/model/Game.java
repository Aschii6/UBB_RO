package model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@Table(name = "Games")
public class Game extends model.Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private Integer points;
    private Integer duration;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Position> potholes;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Position> positions;

    public Game() {}

    public Game(Player player, Integer points, Integer duration, List<Position> potholes, List<Position> positions) {
        this.player = player;
        this.points = points;
        this.duration = duration;
        this.potholes = potholes;
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

    public List<Position> getPotholes() {
        return potholes;
    }

    public void setPotholes(List<Position> potholes) {
        this.potholes = potholes;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", player=" + player +
                ", points=" + points +
                ", duration=" + duration +
                ", potholes=" + potholes +
                ", positions=" + positions +
                '}';
    }
}
