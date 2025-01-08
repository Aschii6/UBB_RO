package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Move implements Serializable {
    private Integer position;
    private Character character;

    public Move() {
    }

    @JsonCreator
    public Move(@JsonProperty("position") Integer position, @JsonProperty("character") Character character) {
        this.position = position;
        this.character = character;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    @Override
    public String toString() {
        return "Move{" +
                "position=" + position +
                ", character=" + character +
                '}';
    }
}
