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

        if ((ParserUtil.relationKeysAndValues(relationshipMap, 0, true) == null
                && ParserUtil.relationKeysAndValues(relationshipMap, 1, true) != null)
                || (ParserUtil.relationKeysAndValues(relationshipMap, 0, true) != null
                && ParserUtil.relationKeysAndValues(relationshipMap, 1, true) == null)) {
            throw new ParseException(Messages.MESSAGE_INVALID_EDIT_RELATIONSHIP_COMMAND_FORMAT);
        }

        String originUuid = ParserUtil.relationKeysAndValues(relationshipMap, 0, false);
        String targetUuid = ParserUtil.relationKeysAndValues(relationshipMap, 1, false);
        String oldRelationshipDescriptor = ParserUtil.relationKeysAndValues(relationshipMap,
                2, false).toLowerCase();
        String newRelationshipDescriptor = ParserUtil.relationKeysAndValues(relationshipMap,
                3, false).toLowerCase();

        if (!newRelationshipDescriptor.endsWith("s")) {
            newRelationshipDescriptor += "s";
        }
        
        if (ParserUtil.relationKeysAndValues(relationshipMap, 0, true) != null) {
            String role1 = ParserUtil.relationKeysAndValues(relationshipMap, 0, true).toLowerCase();
            String role2 = ParserUtil.relationKeysAndValues(relationshipMap, 1, true).toLowerCase();
            if (newRelationshipDescriptor.equals("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
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
}
