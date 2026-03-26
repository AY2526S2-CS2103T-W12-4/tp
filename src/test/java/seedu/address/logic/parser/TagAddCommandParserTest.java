package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.model.tag.Tag;

public class TagAddCommandParserTest {

    private final TagAddCommandParser parser = new TagAddCommandParser();

    @Test
    public void parse_validArgs_success() {
        Tag expectedTag = new Tag("friend");
        Index expectedIndex = Index.fromOneBased(1);
        TagAddCommand expectedCommand = new TagAddCommand(expectedTag, expectedIndex);

        // normal
        assertParseSuccess(parser, "1 t/friend", expectedCommand);

        // leading/trailing whitespace
        assertParseSuccess(parser, "   1   t/friend   ", expectedCommand);
    }

    @Test
    public void parse_missingTagPrefix_failure() {
        // no t/ provided at all
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1", expectedMessage);
    }

    @Test
    public void parse_multipleTags_failure() {
        // more than one t/ should fail with duplicate prefix error
        String expectedMessage = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TAG);
        assertParseFailure(parser, "1 t/friend t/enemy", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        // zero index
        assertParseFailure(parser, "0 t/friend", expectedMessage);

        // non-integer index
        assertParseFailure(parser, "abc t/friend", expectedMessage);

        // missing index (empty input)
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_unknownPrefixInPreamble_failure() {
        // unknown prefix-like token in preamble should be rejected
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 l/123 t/friend", expectedMessage);
    }

    @Test
    public void parse_invalidTag_failure() {
        // tag violates Tag constraints
        assertParseFailure(parser, "1 t/@@@", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyTagValue_failure() {
        // prefix is present but empty value -> treated as invalid tag format
        assertParseFailure(parser, "1 t/", Tag.MESSAGE_CONSTRAINTS);
    }
}
