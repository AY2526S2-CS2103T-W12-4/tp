package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagAddCommand object.
 */
public class TagAddCommandParser implements Parser<TagAddCommand> {

    /**
     * Parses the given arguments string and returns a TagAddCommand.
     *
     * Expected format: INDEX t/TAG
     *
     * @param args the input arguments string
     * @return a TagAddCommand object containing the parsed index and tag
     * @throws ParseException if the input format is invalid
     */
    public TagAddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        String preamble = argMultimap.getPreamble();

        Index index;
        try {
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TAG);

        if (!argMultimap.getValue(PREFIX_TAG).isPresent()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagAddCommand.MESSAGE_USAGE));
        }

        Tag tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());

        return new TagAddCommand(tag, index);
    }
}
