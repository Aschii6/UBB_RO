package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "Configurations")
public class Configuration extends Entity<Long> {
    private Long id;
    private Integer row;
    private Integer column;
    private String clue;

    public Configuration() {
    }

    @JsonCreator
    public Configuration(@JsonProperty("row") Integer row,
                         @JsonProperty("column") Integer column,
                         @JsonProperty("clue") String clue) {
        this.row = row;
        this.column = column;
        this.clue = clue;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", row=" + row +
                ", column=" + column +
                ", clue='" + clue + '\'' +
                '}';
    }
}
