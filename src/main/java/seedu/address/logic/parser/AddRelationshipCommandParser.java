package seedu.address.logic.parser;

import java.util.LinkedHashMap;

import seedu.address.logic.commands.AddRelationshipCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddRelationshipCommand object
 */
public class AddRelationshipCommandParser implements Parser<AddRelationshipCommand> {
    /**
     * Parses a userInput into the arguments to add a relationship to AB3
     *
     * @param userInput user-input command
     * @return an addRelationshipCommand with the necessary arguments
     */
    public AddRelationshipCommand parse(String userInput) throws ParseException {
        LinkedHashMap<String, String> relationshipMap = ParserUtil.getRelationshipHash(userInput, true);
        String originUuid = ParserUtil.relationKeysAndValues(relationshipMap, 0, false);
        String targetUuid = ParserUtil.relationKeysAndValues(relationshipMap, 1, false);
        String relationshipDescriptor = ParserUtil.relationKeysAndValues(relationshipMap, 2, false);
        if (relationshipDescriptor == null
                && ParserUtil.relationKeysAndValues(relationshipMap, 2, true) == null) {
            throw new ParseException("Relationship Descriptor cannot be empty");
        } else if (relationshipDescriptor == null) {
            relationshipDescriptor =
                    ParserUtil.relationKeysAndValues(relationshipMap, 2, true).toLowerCase();
        } else {
            relationshipDescriptor = relationshipDescriptor.toLowerCase();
        }
        return createRelationshipCommand(originUuid, targetUuid, relationshipDescriptor, relationshipMap);
    }

    private AddRelationshipCommand createRelationshipCommand(String originUuid, String targetUuid,
                                                             String descriptor,
                                                             LinkedHashMap<String, String> relationshipMap)
            throws ParseException {
        if (descriptor.equalsIgnoreCase("bioparents") || descriptor.equals("siblings")
                || descriptor.equalsIgnoreCase("spouses")) {
            ParserUtil.validateRolesForFamilialRelation(descriptor, relationshipMap);
            String role1 = ParserUtil.relationKeysAndValues(relationshipMap, 0, true).toLowerCase();
            String role2 = ParserUtil.relationKeysAndValues(relationshipMap, 1, true).toLowerCase();

            return new AddRelationshipCommand(originUuid, targetUuid, descriptor, role1, role2);
        }
        if ((ParserUtil.relationKeysAndValues(relationshipMap, 0, true) != null
                && ParserUtil.relationKeysAndValues(relationshipMap, 1, true) != null)) {
            String role1 = ParserUtil.relationKeysAndValues(relationshipMap, 0, true).toLowerCase();
            String role2 = ParserUtil.relationKeysAndValues(relationshipMap, 1, true).toLowerCase();

            if (descriptor.toLowerCase().contains("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
            }
            return new AddRelationshipCommand(originUuid, targetUuid, descriptor, role1, role2);
        } else {
            if (descriptor.toLowerCase().contains("family")) {
                throw new ParseException("Please specify the type of familial relationship instead of 'Family'.\n"
                        + " Valid familial relations are: [bioParents, siblings, spouses]");
            }
            return new AddRelationshipCommand(originUuid, targetUuid, descriptor);
        }
    }
}
