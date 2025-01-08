package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class Position implements Serializable {
    private Integer row, col;

    public Position() {
    }

    @JsonCreator
    public Position(@JsonProperty("row") Integer row, @JsonProperty("column") Integer col) {
        this.row = row;
        this.col = col;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;
        return Objects.equals(row, position.row) && Objects.equals(col, position.col);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(row);
        result = 31 * result + Objects.hashCode(col);
        return result;
    }

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
