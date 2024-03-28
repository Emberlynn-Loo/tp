package seedu.address.logic.relationship;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input into an EditRelationshipCommand.
 */
public class EditRelationshipCommandParser {
    /**
     * Parses the given user input and returns an EditRelationshipCommand.
     *
     * @param userInput The user input to parse.
     * @return The EditRelationshipCommand corresponding to the user input.
     * @throws IllegalArgumentException If the user input is invalid.
     */
    public EditRelationshipCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        String[] parts = userInput.split("/", -1);
        if (parts.length != 5) {
            throw new ParseException(Messages.MESSAGE_INVALID_EDIT_RELATIONSHIP_COMMAND_FORMAT);
        }
        parts = ParserUtil.removeFirstItemFromStringList(parts);
        LinkedHashMap<String, String> relationshipMap = ParserUtil.getRelationshipHashMapEdit(parts);

        if ((relationKeysAndValues(relationshipMap, 0, true) == null
                && relationKeysAndValues(relationshipMap, 1, true) != null)
                || (relationKeysAndValues(relationshipMap, 0, true) != null
                && relationKeysAndValues(relationshipMap, 1, true) == null)) {
            throw new ParseException(Messages.MESSAGE_INVALID_EDIT_RELATIONSHIP_COMMAND_FORMAT);
        }

        String originUuid = relationKeysAndValues(relationshipMap, 0, false);
        String targetUuid = relationKeysAndValues(relationshipMap, 1, false);
        String oldRelationshipDescriptor = relationKeysAndValues(relationshipMap, 2, false).toLowerCase();
        String newRelationshipDescriptor = relationKeysAndValues(relationshipMap, 3, false).toLowerCase();

        if (relationKeysAndValues(relationshipMap, 0, true) != null) {
            String role1 = relationKeysAndValues(relationshipMap, 0, true).toLowerCase();
            String role2 = relationKeysAndValues(relationshipMap, 1, true).toLowerCase();
            if (newRelationshipDescriptor.equals("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
            }
            if (!role1.matches("[a-zA-Z]+") || !role2.matches("[a-zA-Z]+")) {
                throw new ParseException("Roles must contain only letters");
            }
            return new EditRelationshipCommand(originUuid, targetUuid, oldRelationshipDescriptor,
                    newRelationshipDescriptor, role1, role2);
        } else {
            if (newRelationshipDescriptor.equals("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
            }
            return new EditRelationshipCommand(originUuid, targetUuid, oldRelationshipDescriptor,
                    newRelationshipDescriptor);
        }
    }

    String relationKeysAndValues(LinkedHashMap<String, String> relationshipMap, int index, boolean value) {
        if (index >= relationshipMap.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (!value) {
            return relationshipMap.keySet().toArray(new String[0])[index];
        } else {
            return relationshipMap.values().toArray(new String[0])[index];
        }
    }
}
