package Domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class FriendDTO {
    String firstName;
    String lastName;
    LocalDateTime localDateTime;

    public FriendDTO(String firstName, String lastName, LocalDateTime localDateTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.localDateTime = localDateTime;
    }

    @Override
    public String toString() {
        return  lastName +
                "|" + firstName +
                "|" + localDateTime.toLocalDate();
    }
}
