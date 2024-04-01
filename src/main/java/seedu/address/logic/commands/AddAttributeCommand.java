package seedu.address.logic.commands;

import java.util.Map;
import java.util.UUID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.attribute.Attribute;


/**
 * A command to add a new attribute to a person in the address book, or to update an existing attribute.
 * This command can also be used to delete an attribute by providing a null value for the attribute value.
 */
public class AddAttributeCommand extends Command {

    public static final String COMMAND_WORD = "addattribute";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds attributes to a person in the address book. "
            + "\n"
            + "Command format:  " + COMMAND_WORD + " UUID /attributeName1 attributeValue1 "
            + "/attributeName2 attributeValue2 ...\n"
            + "Example: " + COMMAND_WORD + " /4000 /Name John Doe /Phone 12345678";
    public static final String COMMAND_WORD_SHORT = "aa";
    private final String uuid;
    private final Map<String, String> attributes;

    /**
     * Constructs an EditPersonCommand to add or delete an attribute.
     *
     * @param uuid           The UUID of the person to edit.
     * @param attributes     A map of attribute names to attribute values to add or delete.
     */
    public AddAttributeCommand(String uuid, Map<String, String> attributes) {
        this.uuid = uuid;
        this.attributes = attributes;
    }

    /**
     * Executes the command to add or delete an attribute for a person identified by UUID.
     *
     * @param model The model interface containing the address book data and methods needed to perform operations.
     * @return A CommandResult object containing the result message.
     * @throws CommandException if the person with the specified UUID cannot be found.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        UUID uuidToUse = model.getFullUuid(uuid);
        Person person = model.getPersonByUuid(uuidToUse);
        if (person == null) {
            throw new CommandException("Person not found.");
        }

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String attributeName = entry.getKey();
            String attributeValue = entry.getValue();
            Attribute attribute = AttributeUtil.createAttribute(attributeName, attributeValue);
            person.updateAttribute(attribute);
        }
        return new CommandResult("Attributes updated successfully.");
    }
}

