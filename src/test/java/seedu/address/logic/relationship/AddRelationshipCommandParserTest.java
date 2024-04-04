package seedu.address.logic.relationship;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersonsUuid.getTypicalAddressBook;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.relationship.RoleBasedRelationship;

class AddRelationshipCommandParserTest {
    private Model model;
    private Model expectedModel;
    private AddRelationshipCommandParser parser = new AddRelationshipCommandParser();
    @BeforeEach
    void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    void parse_validInputFamilyWithRoles_success() {
        String userInput = "/0001 parent /0003 child /bioparents";
        AddRelationshipCommand expected = new AddRelationshipCommand("0001", "0003",
                "bioParents", "parent", "child");
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    void parse_validInputWithRoles_success() {
        String userInput = "/0001 Boss /0003 Subordinate /Workbuddies";
        AddRelationshipCommand expected = new AddRelationshipCommand("0001", "0003",
                "Workbuddies", "Boss", "Subordinate");
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    void parse_invalidInputMissingPartsWithRoles_throwsIllegalArgumentException() {
        String userInput = "/ parent /0002 child /bioparents";
        assertParseFailure(parser, userInput, "Relationship format is invalid. "
                + "\nPlease ensure that the relationship is in the format: "
                + "\naddRelation /<UUID1> /<UUID2> /<relationshipDescriptor> or "
                + "\naddRelation /<UUID1> <role1> /<UUID2> <role2> /<relationshipDescriptor>");

        String userInput2 = "/0001 parent /0003 /bioparents";
        assertParseFailure(parser, userInput2, "Relationship format is invalid. "
                + "\nPlease ensure that the relationship is in the format: "
                + "\naddRelation /<UUID1> /<UUID2> /<relationshipDescriptor> or "
                + "\naddRelation /<UUID1> <role1> /<UUID2> <role2> /<relationshipDescriptor>");
    }

    @Test
    void parse_upperAndLowerCaseInputs_success() {
        String userInput = "/0001 parent /0003 child /Bioparents";
        AddRelationshipCommand expected = new AddRelationshipCommand("0001", "0003",
                "bioparents", "parent", "child");
        assertParseSuccess(parser, userInput, expected);

        String userInput2 = "/0001 brother /0003 Brother /siblings";
        AddRelationshipCommand expected2 = new AddRelationshipCommand("0001", "0003",
                "siblings", "brother", "brother");
        assertParseSuccess(parser, userInput2, expected2);

        String userInput3 = "/0001 husband /0003 husband /spouses";
        AddRelationshipCommand expected3 = new AddRelationshipCommand("0001", "0003",
                "spouses", "husband", "husband");
        assertParseSuccess(parser, userInput3, expected3);

        String userInput5 = "/0001 parent /0006 CHILd /Bioparents";
        AddRelationshipCommand expected5 = new AddRelationshipCommand("0001", "0006",
                "bioparents", "parent", "child");
        assertParseSuccess(parser, userInput5, expected5);
    }

    @Test
    void execute_invalidNewRelationshipIfExistingRelationship_throwsCommandException() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0005";
        String relationshipDescriptor = "bioparents";
        String role1 = "parent";
        String role2 = "child";
        UUID person1Uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID person2Uuid = UUID.fromString("00000000-0000-0000-0000-000000000005");
        RoleBasedRelationship existingRelationship = new RoleBasedRelationship(person1Uuid, person2Uuid,
                relationshipDescriptor, role1, role2);
        model.addRelationship(existingRelationship);
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor, role1, role2);
        assertThrows(CommandException.class, () -> addRelationshipCommand.execute(model));
    }

    @Test
    void parse_validInput_success() {
        String userInput = "/0001 brother /0003 sister /siblings";
        AddRelationshipCommand expected = new AddRelationshipCommand("0001",
                "0003", "siblings", "brother", "sister");
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    void parse_invalidInputMissingParts_throwsIllegalArgumentException() {
        String targetUuid = "0001";
        String originUuid = "0003";
        String userInput = targetUuid + "siblings";
        assertParseFailure(parser, userInput, "Relationship format is invalid. "
                + "\nPlease ensure that the relationship is in the format: "
                + "\naddRelation /<UUID1> /<UUID2> /<relationshipDescriptor> or "
                + "\naddRelation /<UUID1> <role1> /<UUID2> <role2> /<relationshipDescriptor>");

        String userInput2 = originUuid + "siblings";
        assertParseFailure(parser, userInput2, "Relationship format is invalid. "
                + "\nPlease ensure that the relationship is in the format: "
                + "\naddRelation /<UUID1> /<UUID2> /<relationshipDescriptor> or "
                + "\naddRelation /<UUID1> <role1> /<UUID2> <role2> /<relationshipDescriptor>");
    }

    @Test
    void parse_missingRole1_throwsParseException() {
        String userInput = "/0001 /0003 /spouses";
        assertParseFailure(parser, userInput, "spouses relationship requires two roles to be specified.\n"
                + "Please specify the roles in the format: "
                + "\naddRelation /<UUID> <role> /<UUID> <role> /spouses");
    }

    @Test
    void parse_missingRole2_throwsParseException() {
        String userInput = "/0001 boss /0003 /siblings";
        assertParseFailure(parser, userInput, "Relationship format is invalid. "
                + "\nPlease ensure that the relationship is in the format: "
                + "\naddRelation /<UUID1> /<UUID2> /<relationshipDescriptor> or "
                + "\naddRelation /<UUID1> <role1> /<UUID2> <role2> /<relationshipDescriptor>");
    }

    @Test
    void parse_missingRoles_throwsParseException() {
        String userInput = "/0001 /0003 /bioparents";
        assertParseFailure(parser, userInput, "bioparents relationship requires two roles to be specified.\n"
                + "Please specify the roles in the format: "
                + "\naddRelation /<UUID> <role> /<UUID> <role> /bioparents");
    }

    @Test
    void validateRolesForFamilialRelationBioparents_validRoles_success() {
        String relationshipDescriptor = "bioparents";
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();
        relationshipMap.put("role1", "parent");
        relationshipMap.put("role2", "child");
        assertDoesNotThrow(() -> ParserUtil.validateRolesForFamilialRelation(relationshipDescriptor, relationshipMap));
    }

    @Test
    void validateRolesForFamilialRelationBioparents_invalidRoles_throwsParseException() {
        String relationshipDescriptor = "workbuds";
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();
        relationshipMap.put("role1", "sibling");
        relationshipMap.put("role2", "child");
        assertThrows(IllegalStateException.class, () ->
                ParserUtil.validateRolesForFamilialRelation(relationshipDescriptor,
                relationshipMap));
    }
}
