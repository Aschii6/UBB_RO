package model;

public class Ranking extends Entity<Long> {
    private String alias;
    private Integer points;
    private Integer duration;

    public Ranking() {
    }

    public Ranking(String alias, Integer points, Integer duration) {
        this.alias = alias;
        this.points = points;
        this.duration = duration;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
}
