package seedu.address.logic.parser;

import java.util.Arrays;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteAttributeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteAttributeCommandParser implements Parser<DeleteAttributeCommand> {

    /**
     *  Parses the given {@code String} of arguments in the context of the DeleteAttributeCommand
     *  and returns an DeleteAttributeCommand object for execution.
     *  Command format: delete /UUID /attributeName1 /attributeName2 ...
     *  Examples:
     *  delete
     *  delete /Name /Phone /address
     */
    public DeleteAttributeCommand parse(String args) throws ParseException {
        args = args.trim();

        // Split the arguments string on spaces after the first "/"
        String[] parts = args.split(" ", 2);

        // Validate command structure
        if (parts.length < 2) {
            throw new ParseException("Invalid command format. Expected format: "
                    + "deleteAttribute /UUID /attributeName1 /attributeName2 ...");
        }

        String uuid = parts[0].substring(1); // Remove the leading "/"

        // Split the rest of the string by "/" to get the UUID and attributes
        String[] attributeParts = parts[1].trim().split("/", -1);

        // The first attribute part after splitting will be the UUID
        if (uuid.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_INVALID_PERSON_UUID + "\n"
                    + DeleteAttributeCommand.MESSAGE_USAGE);
        }

        // Remove the first element (UUID) to keep the rest as attribute names
        String[] attributeNames = ParserUtil.removeFirstItemFromStringList(attributeParts);
        attributeNames = Arrays.stream(attributeNames).map(String::trim).toArray(String[]::new);

        // Validate that there are attribute names to delete
        if (attributeNames.length == 0) {
            throw new ParseException(Messages.MESSAGE_MISSING_ATTRIBUTES + "\n" + DeleteAttributeCommand.MESSAGE_USAGE);
        }

        return new DeleteAttributeCommand(uuid, attributeNames);
    }

}
