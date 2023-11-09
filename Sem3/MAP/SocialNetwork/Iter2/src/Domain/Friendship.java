package Domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Friendship extends Entity<Tuple<Long, Long>> {

    LocalDateTime date;

    public Friendship(Tuple<Long, Long> idsTuple){
        this.setId(idsTuple);

        date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS); // si in DB se pune default
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date.truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "date=" + date +
                ", id=" + id +
                '}';
    }
}
