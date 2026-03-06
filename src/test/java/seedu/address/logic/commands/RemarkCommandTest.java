package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

public class RemarkCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemark_success() {
        Remark remark = new Remark("Some remark");
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, remark);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personToEdit).withRemark(remark.value).build();

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(editedPerson));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        RemarkCommand firstCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("First"));
        RemarkCommand sameAsFirstCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("First"));
        RemarkCommand secondCommand = new RemarkCommand(INDEX_SECOND_PERSON, new Remark("Second"));

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(sameAsFirstCommand));

        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(new ClearCommand()));
        assertFalse(firstCommand.equals(secondCommand));
    }
}
