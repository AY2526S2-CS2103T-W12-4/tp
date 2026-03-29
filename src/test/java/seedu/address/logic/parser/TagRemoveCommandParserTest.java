package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.TagRemoveCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class TagRemoveCommandParserTest {

    @Test
    public void execute_validIndexTag_success() throws Exception {
        Tag tagToRemove = new Tag("friend");

        Person person = new PersonBuilder()
            .withName("Alice")
            .withTags("friend")
            .build();
        Model model = new ModelManager();

        model.addPerson(person);

        TagRemoveCommand command = new TagRemoveCommand(tagToRemove, Index.fromZeroBased(0));

        CommandResult result = command.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(0);

        assertFalse(editedPerson.getTags().contains(tagToRemove));

        assertEquals(
            String.format(TagRemoveCommand.MESSAGE_REMOVE_TAG_SUCCESS,
                tagToRemove.tagName, editedPerson.getName()),
            result.getFeedbackToUser()
        );
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

        Tag tag = new Tag("friend");
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        TagRemoveCommand command = new TagRemoveCommand(tag, outOfBoundIndex);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        TagRemoveCommandParser parser = new TagRemoveCommandParser();

        assertParseFailure(parser, "1 t/INVALID!", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void execute_tagNotPresent_throwsCommandException() {
        Tag existingTag = new Tag("friend");

        Person person = new PersonBuilder()
            .withName("Alice")
            .withTags("friend")
            .build();

        Model model = new ModelManager();
        model.addPerson(person);

        Tag tagToRemove = new Tag("classmate");

        TagRemoveCommand command = new TagRemoveCommand(tagToRemove, Index.fromZeroBased(0));

        assertCommandFailure(command, model, TagRemoveCommand.MESSAGE_REMOVE_TAG_FAILURE);
    }

    @Test
    public void equals() {
        Tag tag1 = new Tag("friend");
        Tag tag2 = new Tag("classmate");

        Index index1 = Index.fromZeroBased(0);
        Index index2 = Index.fromZeroBased(1);

        TagRemoveCommand command1 = new TagRemoveCommand(tag1, index1);
        TagRemoveCommand command2 = new TagRemoveCommand(tag1, index1);
        TagRemoveCommand command3 = new TagRemoveCommand(tag2, index1);
        TagRemoveCommand command4 = new TagRemoveCommand(tag1, index2);

        // same object -> true
        assertEquals(command1, command1);

        // same values -> true
        assertEquals(command1, command2);

        // different tag -> false
        org.junit.jupiter.api.Assertions.assertNotEquals(command1, command3);

        // different index -> false
        org.junit.jupiter.api.Assertions.assertNotEquals(command1, command4);

        // different type -> false
        org.junit.jupiter.api.Assertions.assertNotEquals(command1, 1);

        // null -> false
        org.junit.jupiter.api.Assertions.assertNotEquals(command1, null);
    }
}
