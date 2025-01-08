package model;

import java.util.List;

public class GameDto extends Entity<Long> {
    private String alias;
    private List<Guess> positions;
    private List<Character> configuration;
    private Integer points;

    public GameDto(String alias,List<Guess> positions, List<Character> configuration, Integer points) {
        this.alias = alias;
        this.positions = positions;
        this.configuration = configuration;
        this.points = points;
    }

    public String getAlias() {
        return alias;
    }

    public List<Guess> getPositions() {
        return positions;
    }

    public List<Character> getConfiguration() {
        return configuration;
    }

    public Integer getPoints() {
        return points;
    }


}
