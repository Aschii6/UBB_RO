package com.example.socialnetworkguitwo.domain;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String tag;

    public User(String firstName, String lastName, String tag) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tag = tag;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {

        return "User{" +
                "id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
