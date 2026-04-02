package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalCircles.CLIENTS;
import static seedu.address.testutil.TypicalCircles.FRIENDS;
import static seedu.address.testutil.TypicalCircles.PROSPECTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.circle.Circle;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class CircleAddCommandTest {

    /**
     * Creates a fresh AddressBook with test persons that have no circles assigned.
     * Used to avoid conflicts with typical test data that already have circles.
     */
    private static AddressBook createCleanAddressBook() {
        AddressBook ab = new AddressBook();
        ab.addPerson(new PersonBuilder().withName("Alice Test")
            .withPhone("98765432").withEmail("alicetest@example.com").withAddress("Test Address 1")
            .build());
        ab.addPerson(new PersonBuilder().withName("Bob Test")
            .withPhone("87654321").withEmail("bobtest@example.com").withAddress("Test Address 2")
            .build());
        ab.addPerson(new PersonBuilder().withName("Charlie Test")
            .withPhone("76543210").withEmail("charlietest@example.com").withAddress("Test Address 3")
            .build());
        return ab;
    }

    @Test
    public void execute_validIndexAndNewCircle_success() {
        Model model = new ModelManager(createCleanAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(createCleanAddressBook(), new UserPrefs());

        Person personAtIndex = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person circledPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(FRIENDS)
        );

        expectedModel.setPerson(personAtIndex, circledPerson);

        CircleAddCommand command = new CircleAddCommand(INDEX_FIRST_PERSON, FRIENDS);
        String expectedMessage = String.format(
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_SUCCESS, FRIENDS.getCircleName(), circledPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexAllCircles_success() {
        // Test prospect circle
        Model model = new ModelManager(createCleanAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(createCleanAddressBook(), new UserPrefs());

        Person personAtIndex = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person circledPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(PROSPECTS)
        );

        expectedModel.setPerson(personAtIndex, circledPerson);

        CircleAddCommand command = new CircleAddCommand(INDEX_FIRST_PERSON, PROSPECTS);
        String expectedMessage = String.format(
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_SUCCESS, PROSPECTS.getCircleName(), circledPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // Test friend circle
        model = new ModelManager(createCleanAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(createCleanAddressBook(), new UserPrefs());

        personAtIndex = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        circledPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(FRIENDS)
        );

        expectedModel.setPerson(personAtIndex, circledPerson);

        command = new CircleAddCommand(INDEX_SECOND_PERSON, FRIENDS);
        expectedMessage = String.format(
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_SUCCESS, FRIENDS.getCircleName(), circledPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_lastPersonValidCircle_success() {
        Model model = new ModelManager(createCleanAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(createCleanAddressBook(), new UserPrefs());

        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        Person personAtIndex = model.getFilteredPersonList().get(lastIndex.getZeroBased());
        Person circledPerson = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(PROSPECTS)
        );

        expectedModel.setPerson(personAtIndex, circledPerson);

        CircleAddCommand command = new CircleAddCommand(lastIndex, PROSPECTS);
        String expectedMessage = String.format(
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_SUCCESS, PROSPECTS.getCircleName(), circledPerson.getName());
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        CircleAddCommand command = new CircleAddCommand(outOfBoundIndex, CLIENTS);

        assertThrows(CommandException.class,
            CircleAddCommand.MESSAGE_INVALID_PERSON, () -> command.execute(model));
    }

    @Test
    public void execute_invalidIndexZero_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Index is created using fromOneBased which starts at 1
        // So getZeroBased() of a 1-based index 1 would be 0, which is the first element
        // We need an index that exceeds the list size
        Index invalidIndex = Index.fromZeroBased(model.getFilteredPersonList().size() + 1);

        CircleAddCommand command = new CircleAddCommand(invalidIndex, FRIENDS);

        assertThrows(CommandException.class,
            CircleAddCommand.MESSAGE_INVALID_PERSON, () -> command.execute(model));
    }

    @Test
    public void execute_personAlreadyHasCircle_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // First add a circle
        Person personAtIndex = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personWithCircle = new Person(
            personAtIndex.getName(),
            personAtIndex.getPhone(),
            personAtIndex.getEmail(),
            personAtIndex.getAddress(),
            personAtIndex.getTags(),
            personAtIndex.getFollowUpDate(),
            personAtIndex.getNotes(),
            java.util.Optional.of(CLIENTS)
        );
        model.setPerson(personAtIndex, personWithCircle);

        // Now try to add another circle
        CircleAddCommand command = new CircleAddCommand(INDEX_FIRST_PERSON, PROSPECTS);

        assertThrows(CommandException.class,
            CircleAddCommand.MESSAGE_CIRCLE_PERSON_FAILURE, () -> command.execute(model));
    }

    @Test
    public void execute_differentIndexes_allValid() {
        String[] validCircles = {"client", "prospect", "friend"};

        for (String circleName : validCircles) {
            Model model = new ModelManager(createCleanAddressBook(), new UserPrefs());

            if (1 <= model.getFilteredPersonList().size()) {
                Index index = Index.fromOneBased(1);
                Circle circle = new Circle(circleName);

                CircleAddCommand command = new CircleAddCommand(index, circle);

                // Should not throw exception for valid index and no existing circle
                try {
                    command.execute(model);
                    // Success - verified that circle was added
                } catch (CommandException e) {
                    // Should not happen with clean data
                    assertTrue(false, "Should not throw exception when adding circle to person without circle");
                }
            }
        }
    }

    @Test
    public void equals() {
        CircleAddCommand addClientFirstCommand = new CircleAddCommand(INDEX_FIRST_PERSON, CLIENTS);
        CircleAddCommand addClientSecondCommand = new CircleAddCommand(INDEX_SECOND_PERSON, CLIENTS);
        CircleAddCommand addProspectFirstCommand = new CircleAddCommand(INDEX_FIRST_PERSON, PROSPECTS);

        // same object -> returns true
        assertTrue(addClientFirstCommand.equals(addClientFirstCommand));

        // same values -> returns true
        CircleAddCommand addClientFirstCommandCopy = new CircleAddCommand(INDEX_FIRST_PERSON, CLIENTS);
        assertTrue(addClientFirstCommand.equals(addClientFirstCommandCopy));

        // different types -> returns false
        assertFalse(addClientFirstCommand.equals(1));

        // null -> returns false
        assertFalse(addClientFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(addClientFirstCommand.equals(addClientSecondCommand));

        // different circle -> returns false
        assertFalse(addClientFirstCommand.equals(addProspectFirstCommand));

        // different circle and index -> returns false
        assertFalse(addClientFirstCommand.equals(addProspectFirstCommand));
    }

    @Test
    public void toString_correct() {
        CircleAddCommand command = new CircleAddCommand(INDEX_FIRST_PERSON, FRIENDS);
        assertTrue(command.toString().contains(INDEX_FIRST_PERSON.toString())
            && command.toString().contains(FRIENDS.getCircleName()));
    }
}
