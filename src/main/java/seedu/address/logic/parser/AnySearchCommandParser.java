package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AnySearchCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AnySearchCommand object
 */
public class AnySearchCommandParser implements Parser<AnySearchCommand> {
    /**
     * Parses a userInput into the arguments to search for relationship links between 2 persons
     * @param userInput user-input command
     * @return an AnySearchCommand with the necessary arguments
     */
    public AnySearchCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        String trimmedInput = userInput.trim();
        String[] parts = trimmedInput.split("/");
        if (parts.length != 3) {
            throw new ParseException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
        String originUuid = parts[1].trim();
        String targetUuid = parts[2].trim();
        return new AnySearchCommand(originUuid, targetUuid);
    }
}
