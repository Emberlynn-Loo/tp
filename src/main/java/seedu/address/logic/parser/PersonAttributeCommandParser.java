package seedu.address.logic.parser;

import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddAttributeCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input into an AddAttributeCommand for managing person attributes.
 * This parser converts user input strings into an AddAttributeCommand object
 * that can add attributes to a person in the address book.
 */
public class PersonAttributeCommandParser {

    /**
     * Parses the given user input string into an executable command.
     *
     * @param userInput The user input string representing a command.
     * @return A Command object ready for execution.
     * @throws ParseException If the user input does not conform to the expected format.
     */
    public Command parse(String userInput) throws ParseException {
        userInput = userInput.trim();
        // Split the command to identify the operation and the arguments
        String[] commandParts = userInput.split(" ", 3);

        // Validate command structure
        if (commandParts.length < 3 || !"addAttribute".equalsIgnoreCase(commandParts[0])) {
            throw new ParseException("Invalid command format. Expected format: addAttribute /uuid [attributes]");
        }

        // The second part should be the UUID prefixed with "/"
        if (!commandParts[1].startsWith("/")) {
            throw new ParseException(Messages.MESSAGE_MISSING_UUID + "\n" + AddAttributeCommand.MESSAGE_USAGE);
        }
        String uuid = commandParts[1].substring(1); // Remove the leading "/"

        // Now, split the remaining part by "/" to separate attributes
        String[] attributeParts = commandParts[2].split("/", -1);
        Map<String, String> attributes = new HashMap<>();
        for (String part : attributeParts) {
            part = part.trim();
            if (!part.isEmpty()) {
                String[] keyValue = part.split(" ", 2);
                if (keyValue.length < 2) {
                    throw new ParseException("Invalid attribute format. Each attribute must be followed by a value.");
                }
                // Extract the attribute name and value
                String attributeName = keyValue[0].trim();
                String attributeValue = keyValue[1].trim();
                attributes.put(attributeName, attributeValue);
            }
        }

        if (attributes.isEmpty()) {
            throw new ParseException(Messages.MESSAGE_MISSING_ATTRIBUTES + "\n" + AddAttributeCommand.MESSAGE_USAGE);
        }

        return new AddAttributeCommand(uuid, attributes);
    }
}
