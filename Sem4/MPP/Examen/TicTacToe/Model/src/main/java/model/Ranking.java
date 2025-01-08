package model;

public class Ranking extends Entity<Long> {
    private String alias;
    private Integer points;
    private Integer duration;

    public Ranking(String alias, Integer points, Integer duration) {
        this.alias = alias;
        this.points = points;
        this.duration = duration;
    }

    public String getAlias() {
        return alias;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getDuration() {
        return duration;
    }
}
