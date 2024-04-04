package seedu.address.logic.parser;

import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditAttributeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input into an EditAttributeCommand for managing person attributes.
 * This parser converts user input strings into an EditAttributeCommand object
 * that can edit attributes associated with a person in the address book.
 */
public class EditCommandParser implements Parser<EditAttributeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditAttributeCommand
     * and returns an EditAttributeCommand object for execution.
     * Command format: editAttribute /uuid /attributeName1 attributeValue1 /attributeName2 attributeValue2 ...
     * Examples:
     * editAttribute /4000 /Name John Doe /Phone 12345678
     */
    public EditAttributeCommand parse(String userInput) throws ParseException {
        userInput = userInput.trim();
        int firstSlashIndex = userInput.indexOf('/');
        if (firstSlashIndex == -1) {
            throw new ParseException(Messages.MESSAGE_MISSING_UUID + "\n" + EditAttributeCommand.MESSAGE_USAGE);
        }

        // Find the index of the start of attributes section (the second slash)
        int secondSlashIndex = userInput.indexOf('/', firstSlashIndex + 1);
        // If there's no second slash, the end of the UUID is the end of the string
        int endOfUuidIndex = (secondSlashIndex == -1) ? userInput.length() : secondSlashIndex;

        // Extract UUID, which is between the first slash and the start of the second segment
        String uuid = userInput.substring(firstSlashIndex + 1, endOfUuidIndex).trim();
        if (uuid.contains(" ") || uuid.length() < 2) {
            throw new ParseException(Messages.MESSAGE_INVALID_UUID_FORMAT + "\n" + EditAttributeCommand.MESSAGE_USAGE);
        }

        // Attributes part starts after the second slash, if there is one
        String attributesPart = (secondSlashIndex != -1) ? userInput.substring(secondSlashIndex + 1) : "";

        Map<String, String> attributes = new HashMap<>();
        if (!attributesPart.isEmpty()) {
            // Now, split the remaining part by "/" to separate attributes, considering additional slashes
            String[] attributeParts = attributesPart.split("/", -1);
            for (String part : attributeParts) {
                part = part.trim();
                if (!part.isEmpty()) {
                    String[] keyValue = part.split(" ", 2);
                    if (keyValue.length < 2) {
                        throw new ParseException("Invalid attribute format. Each attribute must have a value.");
                    }
                    // Extract the attribute name and value
                    String attributeName = keyValue[0].trim();
                    String attributeValue = keyValue[1].trim();
                    attributes.put(attributeName, attributeValue);
                }
            }
        }

        if (attributes.isEmpty()) {
            // You may decide to remove this check if attributes are optional
            throw new ParseException(Messages.MESSAGE_MISSING_ATTRIBUTES + "\n" + EditAttributeCommand.MESSAGE_USAGE);
        }

        return new EditAttributeCommand(uuid, attributes);
    }

}
