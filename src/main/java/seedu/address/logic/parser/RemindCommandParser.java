package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import seedu.address.logic.commands.RemindCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RemindCommand object.
 */
public class RemindCommandParser implements Parser<RemindCommand> {

    @Override
    public RemindCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemindCommand.MESSAGE_USAGE));
        }

        if (!trimmedArgs.matches("\\d+")) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        int days = Integer.parseInt(trimmedArgs);
        if (days <= 0) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        return new RemindCommand(days);
    }
}
