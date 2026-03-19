package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class TagRemoveCommandTest {
    @Test
    public void execute_validIndexAndExistingTag_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index index = Index.fromOneBased(1);

        // Get an existing tag from the first person to ensure removal works
        Person personToEdit = model.getFilteredPersonList().get(index.getZeroBased());
        Tag existingTag = personToEdit.getTags().iterator().next();

        Set<Tag> updatedTags = new HashSet<>(personToEdit.getTags());
        updatedTags.removeIf(t -> t.tagName.equals(existingTag.tagName));
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(),
                personToEdit.getEmail(), personToEdit.getAddress(), updatedTags);
        expectedModel.setPerson(personToEdit, editedPerson);

        TagRemoveCommand command = new TagRemoveCommand(existingTag, index);
        String expectedMessage = String.format(
                TagRemoveCommand.MESSAGE_REMOVE_TAG_SUCCESS, existingTag.tagName, editedPerson.getName());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Tag tag = new Tag("friends");

        TagRemoveCommand command = new TagRemoveCommand(tag, outOfBoundIndex);

        assertThrows(CommandException.class,
                TagRemoveCommand.MESSAGE_INVALID_PERSON, () -> command.execute(model));
    }
}
