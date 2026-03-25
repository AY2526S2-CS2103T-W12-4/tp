package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.FollowUpDate;
import seedu.address.model.person.Person;

/**
 * Sets the follow-up date of an existing person in the address book.
 */
public class SetFollowUpCommand extends Command {

    public static final String COMMAND_WORD = "followup";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the follow up date of the person identified by the index number used "
            + "in the displayed person list.\n"
            + "Parameters: INDEX d/YYYY-MM-DD\n"
            + "Example: " + COMMAND_WORD + " 1 d/2026-03-25";

    public static final String MESSAGE_SET_FOLLOW_UP_SUCCESS =
            "Set follow up date for Person: %1$s";

    private static final Logger logger = LogsCenter.getLogger(SetFollowUpCommand.class);

    private final Index targetIndex;
    private final FollowUpDate followUpDate;

    /**
     * Creates a SetFollowUpCommand to set the follow-up date of the person at the specified index.
     */
    public SetFollowUpCommand(Index targetIndex, FollowUpDate followUpDate) {
        requireNonNull(targetIndex);
        requireNonNull(followUpDate);
        this.targetIndex = targetIndex;
        this.followUpDate = followUpDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert followUpDate != null;

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(targetIndex.getZeroBased());

        Person editedPerson = new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                personToEdit.getTags(),
                Optional.of(followUpDate),
                personToEdit.getNotes(),
                personToEdit.getCircle()
        );

        logger.fine("Setting follow up date for person at index "
                + targetIndex.getOneBased() + " to " + followUpDate);

        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(
                String.format(MESSAGE_SET_FOLLOW_UP_SUCCESS, Messages.format(editedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SetFollowUpCommand)) {
            return false;
        }

        SetFollowUpCommand otherCommand = (SetFollowUpCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && followUpDate.equals(otherCommand.followUpDate);
    }

    @Override
    public String toString() {
        return SetFollowUpCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", followUpDate=" + followUpDate + "}";
    }
}
