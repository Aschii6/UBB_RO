package model;

public class Player extends Entity<Long> {
    private String alias;

    public Player(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {
        return "Player{" +
                "alias='" + alias + '\'' +
                ", id=" + id +
                '}';
    }
}
