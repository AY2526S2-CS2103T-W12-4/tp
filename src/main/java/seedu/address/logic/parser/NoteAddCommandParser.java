package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new NoteAddCommand object.
 * Expected format: INDEX note/NOTE_TEXT
 * Example: 1 note/Looking for family coverage
 */
public class NoteAddCommandParser implements Parser<NoteAddCommand> {

    public static final int MAX_WORD_COUNT = 200;
    public static final String MESSAGE_WORD_LIMIT_EXCEEDED =
            "Note exceeds the maximum word count of " + MAX_WORD_COUNT + " words.";

    @Override
    public NoteAddCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NOTE);

        // Validate index
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE), pe);
        }

        // Validate note/ prefix is present
        if (argMultimap.getValue(PREFIX_NOTE).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }

        String noteText = argMultimap.getValue(PREFIX_NOTE).get().trim();

        // Validate note is not empty
        if (noteText.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteAddCommand.MESSAGE_USAGE));
        }

        // Validate word count of the new input alone
        // (combined total is validated inside NoteAddCommand.execute())
        if (noteText.split("\\s+").length > MAX_WORD_COUNT) {
            throw new ParseException(MESSAGE_WORD_LIMIT_EXCEEDED);
        }

        return new NoteAddCommand(index, noteText);
    }
}
