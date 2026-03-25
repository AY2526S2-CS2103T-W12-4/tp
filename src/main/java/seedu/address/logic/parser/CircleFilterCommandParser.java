package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.CircleFilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.circle.Circle;

/**
 * Parses input arguments and creates a new CircleFilterCommand object.
 */
public class CircleFilterCommandParser implements Parser<CircleFilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CircleFilterCommand
     * and returns a CircleFilterCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public CircleFilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                CircleFilterCommand.MESSAGE_USAGE));
        }

        String[] splitArgs = trimmedArgs.split("\\s+");
        if (splitArgs.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                CircleFilterCommand.MESSAGE_USAGE));
        }

        if (!Circle.isValidCircleName(trimmedArgs)) {
            throw new ParseException(Circle.MESSAGE_CONSTRAINTS);
        }

        return new CircleFilterCommand(trimmedArgs);
    }
}
