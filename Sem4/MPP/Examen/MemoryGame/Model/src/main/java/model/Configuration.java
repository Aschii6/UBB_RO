package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;

import java.util.List;

@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "Configurations")
public class Configuration extends Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Character> configuration;

    public Configuration() {
    }

    @JsonCreator
    public Configuration(List<Character> configuration) {
        this.configuration = configuration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Character> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(List<Character> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", configuration=" + configuration +
                '}';
    }
}
