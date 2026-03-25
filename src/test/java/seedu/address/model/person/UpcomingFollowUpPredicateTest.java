package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class UpcomingFollowUpPredicateTest {

    @Test
    public void equals() {
        UpcomingFollowUpPredicate firstPredicate = new UpcomingFollowUpPredicate(3);
        UpcomingFollowUpPredicate secondPredicate = new UpcomingFollowUpPredicate(7);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new UpcomingFollowUpPredicate(3)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(3));
    }

    @Test
    public void test_noFollowUpDate_returnsFalse() {
        UpcomingFollowUpPredicate predicate = new UpcomingFollowUpPredicate(3);
        Person person = new PersonBuilder().build();

        assertFalse(predicate.test(person));
    }

    @Test
    public void test_followUpDateToday_returnsTrue() {
        UpcomingFollowUpPredicate predicate = new UpcomingFollowUpPredicate(3);
        String today = LocalDate.now().toString();
        Person person = new PersonBuilder().withFollowUpDate(today).build();

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_followUpDateWithinRange_returnsTrue() {
        UpcomingFollowUpPredicate predicate = new UpcomingFollowUpPredicate(3);
        String upcomingDate = LocalDate.now().plusDays(2).toString();
        Person person = new PersonBuilder().withFollowUpDate(upcomingDate).build();

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_followUpDateOutOfRange_returnsFalse() {
        UpcomingFollowUpPredicate predicate = new UpcomingFollowUpPredicate(3);
        String farDate = LocalDate.now().plusDays(5).toString();
        Person person = new PersonBuilder().withFollowUpDate(farDate).build();

        assertFalse(predicate.test(person));
    }
}
