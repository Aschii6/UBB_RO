package model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "Players")
public class Player extends Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    String alias;

    public Player() {}

    public Player(String alias) {
        this.alias = alias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                '}';
    }
}
