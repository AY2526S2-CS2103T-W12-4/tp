package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_confirmed_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        ClearCommand command = new ClearCommand(msg -> true);

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_CLEAR_SUCCESS, expectedModel);
    }

    @Test
    public void execute_emptyAddressBook_cancelled_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        ClearCommand command = new ClearCommand(msg -> false);

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_CLEAR_CANCELLED, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_confirmed_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        ClearCommand command = new ClearCommand(msg -> true);

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_CLEAR_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_cancelled_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        ClearCommand command = new ClearCommand(msg -> false);

        assertCommandSuccess(command, model, ClearCommand.MESSAGE_CLEAR_CANCELLED, expectedModel);
    }
}
