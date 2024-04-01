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
    public EditAttributeCommand parse(String args) throws ParseException {
        args = args.trim();

        // Split the arguments string on spaces after the first "/"
        String[] parts = args.split(" ", 3);

        // Validate command structure
        if (parts.length < 3) {
            throw new ParseException(EditAttributeCommand.MESSAGE_USAGE);
        }

        // The second part should be the UUID prefixed with "/"
        if (!parts[1].startsWith("/")) {
            throw new ParseException(Messages.MESSAGE_MISSING_UUID + "\n" + EditAttributeCommand.MESSAGE_USAGE);
        }
        String uuid = parts[1].substring(1); // Remove the leading "/"

        if (parts.length < 2) {
            throw new ParseException(Messages.MESSAGE_MISSING_ATTRIBUTES + "\n" + EditAttributeCommand.MESSAGE_USAGE);
        }

        // The rest of the string will be attribute/value pairs split by "/"
        String[] attributePairs = parts[2].split("/", -1);
        Map<String, String> attributesToEdit = new HashMap<>();
        for (String pair : attributePairs) {
            pair = pair.trim();
            if (pair.isEmpty()) {
                continue;
            }

            // Split each pair by the first space
            String[] keyValue = pair.split(" ", 2);
            if (keyValue.length < 2) {
                throw new ParseException(Messages.MESSAGE_INVALID_ATTRIBUTE_FORMAT + "\n"
                        + EditAttributeCommand.MESSAGE_USAGE);
            }
            String attributeName = keyValue[0].trim();
            String attributeValue = keyValue[1].trim();

            // Check that the attribute name isn't empty and the attribute value isn't just whitespace
            if (attributeName.isEmpty() || attributeValue.trim().isEmpty()) {
                throw new ParseException(Messages.MESSAGE_INVALID_ATTRIBUTE_FORMAT + "\n"
                        + EditAttributeCommand.MESSAGE_USAGE);
            }

            attributesToEdit.put(attributeName, attributeValue);
        }

        return new EditAttributeCommand(uuid, attributesToEdit);
    }

}
