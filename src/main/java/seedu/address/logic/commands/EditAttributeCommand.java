package seedu.address.logic.commands;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.attribute.Attribute;
import seedu.address.model.person.attribute.BirthdayAttribute;
import seedu.address.model.person.attribute.NameAttribute;
import seedu.address.model.person.attribute.PhoneNumberAttribute;
import seedu.address.model.person.attribute.SexAttribute;

/**
 * A command to edit an existing attribute of a person in the address book.
 * The command requires the attribute to exist before it can be edited.
 */
public class EditAttributeCommand extends Command {

    public static final String COMMAND_WORD = "editAttribute";
    public static final Object MESSAGE_USAGE = COMMAND_WORD + ": Edits attributes of a person in the address book. "
            + "\n"
            + "Command format:  " + COMMAND_WORD + " /UUID /attributeName1 attributeValue1 "
            + "/attributeName2 attributeValue2 ...\n"
            + "Example: " + COMMAND_WORD + " /4000 /Name John Doe /Phone 12345678";
    private final String uuid;
    private final Map<String, String> attributesToEdit;

    /**
     * Constructs an EditAttributeCommand to edit existing attributes.
     *
     * @param uuid           The UUID of the person to edit.
     * @param attributesToEdit A map of attribute names to their new values.
     */
    public EditAttributeCommand(String uuid, Map<String, String> attributesToEdit) {
        this.uuid = uuid;
        this.attributesToEdit = attributesToEdit;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        UUID uuidToUse = model.getFullUuid(uuid);
        Person person = model.getPersonByUuid(uuidToUse);
        if (person == null) {
            throw new CommandException("Person not found.");
        }

        for (Map.Entry<String, String> entry : attributesToEdit.entrySet()) {
            String attributeName = entry.getKey();
            String attributeValue = entry.getValue();

            // Check if the person already has the attribute
            if (!person.hasAttribute(attributeName)) {
                throw new CommandException(String.format("Attribute %s does not exist and cannot be edited.",
                        attributeName));
            }

            Attribute attribute;

            // Similar switch-case logic as AddAttributeCommand for handling different attribute types
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
            case "sex":
                if ("male".equalsIgnoreCase(attributeValue)) {
                    attribute = new SexAttribute("Sex", SexAttribute.Gender.MALE);
                } else if ("female".equalsIgnoreCase(attributeValue)) {
                    attribute = new SexAttribute("Sex", SexAttribute.Gender.FEMALE);
                } else {
                    throw new CommandException("Sex for " + attributeName + " can be either male or female.");
                }
                break;
            default:
                attribute = Attribute.fromString(attributeName, attributeValue);
                if (attribute == null) {
                    throw new CommandException("Unknown or invalid attribute " + attributeName + ".");
                }
            }

            person.updateAttribute(attribute);
        }

        return new CommandResult("Attributes edited successfully.");
    }
}
