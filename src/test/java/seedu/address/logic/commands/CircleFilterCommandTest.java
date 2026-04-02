package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalCircles.CLIENTS;
import static seedu.address.testutil.TypicalCircles.FRIENDS;
import static seedu.address.testutil.TypicalCircles.PROSPECTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.circle.Circle;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class CircleFilterCommandTest {

    private static final String CLIENT = CLIENTS.getCircleName();
    private static final String PROSPECT = PROSPECTS.getCircleName();
    private static final String FRIEND = FRIENDS.getCircleName();

    @Test
    public void execute_filterByClient_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        setCircleAtIndex(model, INDEX_FIRST_PERSON, Optional.of(CLIENTS));

        CircleFilterCommand command = new CircleFilterCommand(CLIENT);
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains(CLIENT));
    }

    @Test
    public void execute_filterByProspect_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        setCircleAtIndex(model, INDEX_SECOND_PERSON, Optional.of(PROSPECTS));

        CircleFilterCommand command = new CircleFilterCommand(PROSPECT);
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains(PROSPECT));
    }

    @Test
    public void execute_filterByFriend_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        setCircleAtIndex(model, Index.fromOneBased(3), Optional.of(FRIENDS));

        CircleFilterCommand command = new CircleFilterCommand(FRIEND);
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains(FRIEND));
    }

    @Test
    public void execute_filterNoContactsFound_returnsNoContactsMessage() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        List<Person> originalList = model.getFilteredPersonList();
        for (int i = 0; i < originalList.size(); i++) {
            setCircleAtIndex(model, Index.fromZeroBased(i), Optional.empty());
        }

        CircleFilterCommand command = new CircleFilterCommand(CLIENT);
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains("No contacts found"));
    }

    @Test
    public void execute_filterCaseInsensitive_success() throws CommandException {
        String[] caseVariations = {"client", "CLIENT", "Client", "CLiEnT"};
        for (String caseVariation : caseVariations) {
            Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
            setCircleAtIndex(model, INDEX_FIRST_PERSON, Optional.of(CLIENTS));

            CircleFilterCommand command = new CircleFilterCommand(caseVariation);
            CommandResult result = command.execute(model);

            assertTrue(result.getFeedbackToUser().toLowerCase().contains("filtered"));
        }
    }

    @Test
    public void execute_multiplePersonsWithSameCircle() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        setCircleAtIndex(model, INDEX_FIRST_PERSON, Optional.of(PROSPECTS));
        setCircleAtIndex(model, INDEX_SECOND_PERSON, Optional.of(PROSPECTS));

        CircleFilterCommand command = new CircleFilterCommand(PROSPECT);
        CommandResult result = command.execute(model);

        assertTrue(result.getFeedbackToUser().contains(PROSPECT));
        List<Person> filteredList = model.getFilteredPersonList();
        assertTrue(filteredList.size() >= 2);
    }

    @Test
    public void execute_mixedCircles_filtersByCorrectCircle() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        setCircleAtIndex(model, INDEX_FIRST_PERSON, Optional.of(CLIENTS));
        setCircleAtIndex(model, INDEX_SECOND_PERSON, Optional.of(PROSPECTS));
        setCircleAtIndex(model, Index.fromOneBased(3), Optional.of(FRIENDS));

        CircleFilterCommand command = new CircleFilterCommand(CLIENT);
        command.execute(model);

        List<Person> filteredList = model.getFilteredPersonList();
        for (Person person : filteredList) {
            assertTrue(person.getCircle().isPresent());
            assertTrue(person.getCircle().get().getCircleName().equals(CLIENT));
        }
    }

    @Test
    public void equals() {
        CircleFilterCommand filterClientFirst = new CircleFilterCommand("client");
        CircleFilterCommand filterClientSecond = new CircleFilterCommand("client");
        CircleFilterCommand filterProspect = new CircleFilterCommand("prospect");

        // same object -> returns true
        assertTrue(filterClientFirst.equals(filterClientFirst));

        // same values -> returns true
        assertTrue(filterClientFirst.equals(filterClientSecond));

        // different types -> returns false
        assertFalse(filterClientFirst.equals(1));

        // null -> returns false
        assertFalse(filterClientFirst.equals(null));

        // different circle name -> returns false
        assertFalse(filterClientFirst.equals(filterProspect));
    }

    @Test
    public void equals_caseInsensitive() {
        // Different case should still be equal after normalization
        CircleFilterCommand filterClientLower = new CircleFilterCommand("client");
        CircleFilterCommand filterClientUpper = new CircleFilterCommand("CLIENT");

        // Both normalized to lowercase, so should be equal
        assertTrue(filterClientLower.equals(filterClientUpper));
    }

    @Test
    public void toString_correct() {
        CircleFilterCommand command = new CircleFilterCommand("client");

        assertTrue(command.toString().contains("circleName") && command.toString().contains("client"));
    }

    private void setCircleAtIndex(Model model, Index index, Optional<Circle> circle) {
        Person person = model.getFilteredPersonList().get(index.getZeroBased());
        Person updatedPerson = circle.isPresent()
                ? new PersonBuilder(person).withCircle(circle.get().getCircleName()).build()
                : buildPersonWithCircle(person, Optional.empty());

        model.setPerson(person, updatedPerson);
    }

    private Person buildPersonWithCircle(Person base, Optional<Circle> circle) {
        return new Person(
                base.getName(),
                base.getPhone(),
                base.getEmail(),
                base.getAddress(),
                base.getTags(),
                base.getFollowUpDate(),
                base.getNotes(),
                circle
        );
    }
}
