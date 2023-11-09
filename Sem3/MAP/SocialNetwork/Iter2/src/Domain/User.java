package Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
//    private List<User> friends = new ArrayList<>();

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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

    /*public List<User> getFriends() {
        return friends;
    }*/

    @Override
    public String toString() {
        /*StringBuilder friendsString = new StringBuilder();
        for (User friend : friends) {
            friendsString.append(friend.getFirstName()).append(" ").append(friend.getLastName()).append(", ");
        }

        if (!friendsString.isEmpty())
            friendsString.delete(friendsString.length() - 2, friendsString.length());
         */

        return "User{" +
                "id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}