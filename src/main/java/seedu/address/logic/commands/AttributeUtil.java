package seedu.address.logic.commands;

import java.time.LocalDate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.attribute.Attribute;
import seedu.address.model.person.attribute.BirthdayAttribute;
import seedu.address.model.person.attribute.NameAttribute;
import seedu.address.model.person.attribute.PhoneNumberAttribute;

/**
 * A utility class for creating attributes.
 */
public class AttributeUtil {

    /**
     * Creates an attribute based on the attribute name and value.
     *
     * @param attributeName  The name of the attribute.
     * @param attributeValue The value of the attribute.
     * @return The attribute object created.
     * @throws CommandException If the attribute name or value is invalid.
     */
    public static Attribute createAttribute(String attributeName, String attributeValue) throws CommandException {
        Attribute attribute;

        switch (attributeName.toLowerCase()) {
        case "birthday":
            try {
                LocalDate attributeValueDate = LocalDate.parse(attributeValue);
                attribute = new BirthdayAttribute("Birthday", attributeValueDate);
            } catch (Exception e) {
                throw new CommandException("Invalid date format for " + attributeName + ". Please use yyyy-mm-dd.");
            }
            break;
        case "name":
            attribute = new NameAttribute("Name", attributeValue);
            break;
        case "phone":
            int phoneNumber;
            try {
                phoneNumber = Integer.parseInt(attributeValue);
                if (phoneNumber < 0) {
                    throw new CommandException("Phone number cannot be negative for " + attributeName + ".");
                }
            } catch (NumberFormatException e) {
                throw new CommandException("Phone number for " + attributeName + " must be a number.");
            }
            attribute = new PhoneNumberAttribute("Phone", phoneNumber);
            break;
        default:
            attributeName = capitalizeAttributeName(attributeName);
            attribute = Attribute.fromString(attributeName, attributeValue);
        }
        return attribute;
    }

    /**
     * Capitalizes the first letter of the attribute name.
     *
     * @param attributeName The name of the attribute.
     * @return The attribute name with the first letter capitalized.
     */
    public static String capitalizeAttributeName(String attributeName) {
        if (attributeName == null || attributeName.isEmpty()) {
            return attributeName;
        }

        if (attributeName.length() == 1) {
            return attributeName.toUpperCase();
        }

        return attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
    }
}
