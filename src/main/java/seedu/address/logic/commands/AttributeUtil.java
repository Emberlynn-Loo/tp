package seedu.address.logic.commands;

import java.time.LocalDate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.attribute.Attribute;
import seedu.address.model.person.attribute.BirthdayAttribute;
import seedu.address.model.person.attribute.NameAttribute;
import seedu.address.model.person.attribute.PhoneNumberAttribute;
import seedu.address.model.person.attribute.SexAttribute;

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
            } catch (IllegalArgumentException e) {
                throw new CommandException("Invalid date " + attributeName + ". Please input a date "
                        + "no earlier than today.");
            } catch (Exception e) {
                throw new CommandException("Invalid date " + attributeName + ". Please input a date in the format "
                        + "yyyy-mm-dd. Additionally, please make sure the dates input are valid including leap years.");
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
                throw new CommandException("Phone number for " + attributeName + " must be a number. "
                        + "A number is only valid if it is a positive integer. "
                        + "Additionally, please make sure the phone number is lesser than 9 digits.");
            }
            attribute = new PhoneNumberAttribute("Phone", phoneNumber);
            break;
        case "sex":
            SexAttribute.Gender gender;
            if (attributeValue == null || attributeValue.isEmpty()) {
                throw new CommandException("Sex cannot be empty for " + attributeName + ".");
            } else if (attributeValue.equalsIgnoreCase("female")
                    || attributeValue.equalsIgnoreCase("f")) {
                gender = SexAttribute.Gender.FEMALE;
            } else if (attributeValue.equalsIgnoreCase("male")
                    || attributeValue.equalsIgnoreCase("m")) {
                gender = SexAttribute.Gender.MALE;
            } else {
                throw new CommandException("Sex must only be male or female for " + attributeName + ".");
            }
            attribute = new SexAttribute("Sex", gender);
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

        return attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1).toLowerCase();
    }
}
