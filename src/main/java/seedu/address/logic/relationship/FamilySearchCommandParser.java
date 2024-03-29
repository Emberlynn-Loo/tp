package seedu.address.logic.relationship;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FamilySearchCommand object
 */
public class FamilySearchCommandParser implements Parser<FamilySearchCommand> {
    /**
     * Parses a userInput into the arguments to search for family relationship links between 2 persons
     * @param userInput user-input command
     * @return an FamilySearchCommand with the necessary arguments
     */
    public FamilySearchCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        String trimmedInput = userInput.trim();
        String[] parts = trimmedInput.split("/");
        if (parts.length != 3) {
            throw new ParseException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
        String originUuid = parts[1].trim();
        String targetUuid = parts[2].trim();
        return new FamilySearchCommand(originUuid, targetUuid);
    }
}
