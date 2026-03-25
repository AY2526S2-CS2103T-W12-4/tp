package seedu.address.model.person;

import java.time.LocalDate;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s follow up date falls within the next specified number of days.
 */
public class UpcomingFollowUpPredicate implements Predicate<Person> {

    private final int days;

    public UpcomingFollowUpPredicate(int days) {
        this.days = days;
    }

    @Override
    public boolean test(Person person) {
        if (person.getFollowUpDate().isEmpty()) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate followUpDate = person.getFollowUpDate().get().value;
        LocalDate endDate = today.plusDays(days);

        return !followUpDate.isBefore(today) && !followUpDate.isAfter(endDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UpcomingFollowUpPredicate)) {
            return false;
        }

        UpcomingFollowUpPredicate otherPredicate = (UpcomingFollowUpPredicate) other;
        return days == otherPredicate.days;
    }
}
