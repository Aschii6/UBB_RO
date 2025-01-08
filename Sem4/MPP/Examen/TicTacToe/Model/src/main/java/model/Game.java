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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Move> moves;

    public Game() {
    }

    public Game(Player player, Integer points, Integer duration, List<Move> moves) {
        this.player = player;
        this.points = points;
        this.duration = duration;
        this.moves = moves;
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

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", player=" + player +
                ", points=" + points +
                ", duration=" + duration +
                ", moves=" + moves +
                '}';
    }
}
