package seedu.address.logic.relationship;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddRelationshipCommand object
 */
public class AddRelationshipCommandParser implements Parser<AddRelationshipCommand> {
    /**
     * Parses a userInput into the arguments to add a relationship to AB3
     * @param userInput user-input command
     * @return an addRelationshipCommand with the necessary arguments
     */
    public AddRelationshipCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        String[] parts = userInput.split("/", -1);
        if (parts.length != 4) {
            throw new ParseException(Messages.MESSAGE_INVALID_RELATIONSHIP_COMMAND_FORMAT);
        }
        parts = ParserUtil.removeFirstItemFromStringList(parts);
        LinkedHashMap<String, String> relationshipMap = ParserUtil.getRelationshipHashMapFromRelationshipStrings(parts);

        if ((relationKeysAndValues(relationshipMap, 0, true) == null
                && relationKeysAndValues(relationshipMap, 1, true) != null)
                || (relationKeysAndValues(relationshipMap, 0, true) != null
                && relationKeysAndValues(relationshipMap, 1, true) == null)) {
            throw new ParseException(Messages.MESSAGE_INVALID_RELATIONSHIP_COMMAND_FORMAT);
        }

        String originUuid = relationKeysAndValues(relationshipMap, 0, false);
        String targetUuid = relationKeysAndValues(relationshipMap, 1, false);
        String relationshipDescriptor = relationKeysAndValues(relationshipMap, 2, false).toLowerCase();

        if (relationKeysAndValues(relationshipMap, 0, true) != null
                && relationKeysAndValues(relationshipMap, 1, true) != null) {
            String role1 = relationKeysAndValues(relationshipMap, 0, true).toLowerCase();
            String role2 = relationKeysAndValues(relationshipMap, 1, true).toLowerCase();

            originUuid = ParserUtil.parseUuid(originUuid);
            targetUuid = ParserUtil.parseUuid(targetUuid);
            relationshipDescriptor = relationshipDescriptor.toLowerCase();
            if (relationshipDescriptor.equals("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
            }
            return new AddRelationshipCommand(originUuid, targetUuid, relationshipDescriptor, role1, role2);
        } else {
            originUuid = ParserUtil.parseUuid(originUuid);
            targetUuid = ParserUtil.parseUuid(targetUuid);
            relationshipDescriptor = relationshipDescriptor.toLowerCase();
            if (relationshipDescriptor.equals("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
            }
            return new AddRelationshipCommand(originUuid, targetUuid, relationshipDescriptor);
        }
    }

    private String relationKeysAndValues(LinkedHashMap<String, String> relationshipMap, int index, boolean value) {
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
