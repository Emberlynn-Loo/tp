package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {
    public static final String MESSAGE_MISSING_UUID = "Missing UUID.";
    public static final String MESSAGE_NO_SUCH_ATTRIBUTE = "No such attribute(s) found: ";
    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command. Please type 'help' to see the list of "
            + "available commands on our user guide.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    public static final String MESSAGE_INVALID_PERSON_UUID = "The UUID provided is invalid, "
            + "does the person exist and is it 4 characters long? The UUID you entered was: ";
    public static final String MESSAGE_MISSING_ATTRIBUTES = "Missing attributes to delete.";

    public static final String MESSAGE_INVALID_PREDEFINED_RELATIONSHIP_DESCRIPTOR = "You cannot delete this "
            + "relationship type. \nType 'listRelations to see your list of relationship types.'";
    public static final String MESSAGE_INVALID_RELATIONSHIP_COMMAND_FORMAT = "Relationship format is invalid. "
            + "\nPlease ensure that the relationship is in the format: "
            + "\naddRelation /<UUID1> /<UUID2> /<relationshipDescriptor> or "
            + "\naddRelation /<UUID1> <role1> /<UUID2> <role2> /<relationshipDescriptor>";;

    public static final String MESSAGE_INVALID_DELETE_RELATIONSHIP_COMMAND_FORMAT = "Relationship format is invalid. "
            + "\nPlease ensure that the relationship is in the format: "
            + "\ndeleteRelation /<UUID1> /<UUID2> /<relationshipDescriptor> or "
            + "\ndeleteRelation /<relationshipDescriptor>";

    public static final String MESSAGE_INVALID_EDIT_RELATIONSHIP_COMMAND_FORMAT = "Relationship format is invalid. "
            + "\nPlease ensure that the relationship is in the format: "
            + "\neditRelation /<UUID1> /<UUID2> /<oldRelationshipDescriptor> /<newRelationshipDescriptor> "
            + "\nor "
            + "\neditRelation /<UUID1> <role1> /<UUID2> <role2> /<oldRelationshipDescriptor> "
            + "/<newRelationshipDescriptor>";

    public static final String MESSAGE_SEARCH_FAILURE = "No Relationship pathway found";
    public static final String MESSAGE_INVALID_ATTRIBUTE_FORMAT = "Invalid attribute format.";
    public static final String MESSAGE_UUID_EMPTY = "Blank UUID provided. Please provide a valid UUID in this format: "
             + "/UUID" + "\nExample: /d8d8";
    public static final String MESSAGE_INVALID_UUID_FORMAT = "Invalid UUID format. Please ensure that the UUID is "
            + "in the format: /<UUID>.";
    public static final String MESSAGE_DUPLICATE_ATTRIBUTES = "Duplicate attributes found.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Details:\n")
                .append(person.allAttributesAsString());
        return builder.toString();
    }

}

