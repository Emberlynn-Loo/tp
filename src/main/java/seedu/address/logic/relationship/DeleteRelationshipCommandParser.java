package seedu.address.logic.relationship;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input into a DeleteRelationshipCommand.
 */
public class DeleteRelationshipCommandParser implements Parser<DeleteRelationshipCommand> {
    /**
     * Parses the given user input and returns a DeleteRelationshipCommand.
     *
     * @param userInput The user input to parse.
     * @return The DeleteRelationshipCommand corresponding to the user input.
     * @throws IllegalArgumentException If the user input is invalid.
     */
    public DeleteRelationshipCommand parse(String userInput) throws ParseException, CommandException {
        requireNonNull(userInput);
        String[] parts = userInput.split("/", -1);
        if (parts.length != 4 && parts.length != 2) {
            throw new ParseException(Messages.MESSAGE_INVALID_DELETE_RELATIONSHIP_COMMAND_FORMAT);
        }
        parts = ParserUtil.removeFirstItemFromStringList(parts);
        boolean hasUuids = false;
        if (parts.length == 3) {
            hasUuids = true;
        }
        LinkedHashMap<String, String> relationshipMap = ParserUtil.getRelationshipHashMapDelete(parts, hasUuids);

        if (relationshipMap.size() == 3) {
            String originUuid = ParserUtil.relationKeysAndValues(relationshipMap, 0, false);
            String targetUuid = ParserUtil.relationKeysAndValues(relationshipMap, 1, false);
            String relationshipDescriptor = ParserUtil.relationKeysAndValues(relationshipMap,
                    2, false).toLowerCase();
            if (!relationshipDescriptor.endsWith("s")) {
                relationshipDescriptor += "s";
            }
            if (relationshipDescriptor.equals("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
            }
            return new DeleteRelationshipCommand(originUuid, targetUuid, relationshipDescriptor);
        } else {
            String relationshipDescriptor = ParserUtil.relationKeysAndValues(relationshipMap,
                    0, false).toLowerCase();
            if (!relationshipDescriptor.endsWith("s")) {
                relationshipDescriptor += "s";
            }
            if (relationshipDescriptor.equals("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
            }

            if (relationshipDescriptor.equals("friend") || relationshipDescriptor.equals("bioparents")
                    || relationshipDescriptor.equals("siblings") || relationshipDescriptor.equals("spouses")) {
                throw new ParseException(Messages.MESSAGE_INVALID_PREDEFINED_RELATIONSHIP_DESCRIPTOR);
            }
            return new DeleteRelationshipCommand(relationshipDescriptor, true);
        }
    }
}
