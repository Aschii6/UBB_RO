package model;

import java.io.Serializable;

public class Guess implements Serializable {
    private Integer row, column;

    public Guess(Integer row, Integer column) {
        this.row = row;
        this.column = column;
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

    @Override
    public String toString() {
        return "Guess{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
