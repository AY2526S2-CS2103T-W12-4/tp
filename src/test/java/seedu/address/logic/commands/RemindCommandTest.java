package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.UpcomingFollowUpPredicate;
import seedu.address.testutil.PersonBuilder;

public class RemindCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validDays_filtersList() throws Exception {
        Person personOne = new PersonBuilder(model.getFilteredPersonList().get(0))
                .withFollowUpDate(LocalDate.now().toString())
                .build();
        model.setPerson(model.getFilteredPersonList().get(0), personOne);
        expectedModel.setPerson(expectedModel.getFilteredPersonList().get(0), personOne);

        Person personTwo = new PersonBuilder(model.getFilteredPersonList().get(1))
                .withFollowUpDate(LocalDate.now().plusDays(2).toString())
                .build();
        model.setPerson(model.getFilteredPersonList().get(1), personTwo);
        expectedModel.setPerson(expectedModel.getFilteredPersonList().get(1), personTwo);

        RemindCommand command = new RemindCommand(3);
        expectedModel.updateFilteredPersonList(new UpcomingFollowUpPredicate(3));

        CommandResult commandResult = command.execute(model);

        assertEquals(String.format(RemindCommand.MESSAGE_SUCCESS, 3), commandResult.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void equals() {
        RemindCommand firstCommand = new RemindCommand(3);
        RemindCommand secondCommand = new RemindCommand(7);

        assertEquals(firstCommand, firstCommand);
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, secondCommand);
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, null);
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, new ClearCommand());
    }
}
