package seedu.address.logic.relationship;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

public class EditRelationshipCommandParserTest {
    private EditRelationshipCommandParser parser = new EditRelationshipCommandParser();

    @Test
    public void parse_invalidInputMissingParts_throwsParseException() {
        String userInput = "uuid1 uuid2 siblings";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputInvalidUuid_throwsParseException() {
        String userInput = "invalidUuid uuid2 siblings friend";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputInvalidRelationshipDescriptors_throwsParseException() {
        String userInput = "uuid1 uuid2 invalidRelationship friend";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputSameDescriptors_throwsParseException() {
        String userInput = "uuid1 uuid2 siblings siblings";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }


    @Test
    public void parse_validInputWithoutRoles_success() {
        // Valid input with UUIDs and relationship descriptors
        String userInput = "/1234 "
                + "/5432 "
                + "/siblings /friend";

        EditRelationshipCommandParser parser = new EditRelationshipCommandParser();

        try {
            EditRelationshipCommand command = parser.parse(userInput);
            assertNotNull(command);
        } catch (ParseException e) {
            fail("Unexpected ParseException thrown");
        }
    }

    @Test
    public void parse_validInputWithRoles_success() {
        // Valid input with UUIDs and relationship descriptors
        String userInput = "/Boss 1234 "
                + "/subordinate 5432 "
                + "/siblings /workbuddies";

        EditRelationshipCommandParser parser = new EditRelationshipCommandParser();

        assertThrows(ParseException.class, () -> parser.parse(userInput),
                "Expected a ParseException to be thrown");
    }

    @Test
    public void parse_validInputWithRoleInvalidNewRelationshipDescriptor_throwsParseException() {
        String userInput = "/1234 role /1245 role /oldDescriptor /smt";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputInvalidRole_throwsParseException() {
        String userInput = "/1234 123 /1222 role /oldDescriptor /newDescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputMissingPartsWithRoles_throwsParseException() {
        String userInput = "/1234 Role /1233 Role /oldDescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputWithFamilyDescriptor_throwsParseException() {
        String userInput = "/1234 role /1256 role /family /newDescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_invalidRoleInput_throwsIllegalArgumentException() {
        String userInput = "/0001 parent /0002 123 /friends /bioparents";
        assertThrows(ParseException.class, () -> parser.parse(userInput));

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputWrongFormat_exceptionThrown() {
        // Invalid input with wrong format
        String userInput = "invalid input";

        EditRelationshipCommandParser parser = new EditRelationshipCommandParser();

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputInvalidUuid_exceptionThrown() {
        // Invalid input with invalid UUIDs
        String userInput = "invalid-uuid invalid-uuid oldDescriptor newDescriptor";

        EditRelationshipCommandParser parser = new EditRelationshipCommandParser();

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void execute_editSameDescriptor_throwsCommandException() {
        String userInput = "/0001 /0002 /des /des";
        EditRelationshipCommandParser parser = new EditRelationshipCommandParser();
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void execute_editSameDescriptorWithRoles_throwsCommandException() {
        String userInput = "/0001 smt /0002 smtelse /des /des";
        EditRelationshipCommandParser parser = new EditRelationshipCommandParser();
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputMissingRole2_throwsParseException() {
        // Missing Role2
        String userInput = "/role1 /uuid1 /uuid2 /oldDescriptor /newDescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputMissingNewDescriptor_throwsParseException() {
        // Missing newDescriptor
        String userInput = "/role1 /uuid1 /role2 /uuid2 /oldDescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputMissingOldDescriptor_throwsParseException() {
        // Missing oldDescriptor
        String userInput = "/role1 /uuid1 /role2 /uuid2 /newDescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputMissingRole2AndNewDescriptor_throwsParseException() {
        String userInput = "/1234 role /1244 /oldDescriptor /newdescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidInputMissingRole1AndOldDescriptor_throwsParseException() {
        // Missing Role1 and oldDescriptor
        String userInput = "/1234 /1244 role /oldDescriptor /newdescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void getRelationshipHashMapEdit_bothKeysNull_throwsParseException() {
        String userInput = "/1234 smt /1244 role /oldDescriptor /family";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void getRelationshipHashMapEdit_throwsParseException() {
        String userInput = "/1234 /1244 /oldDescriptor /family";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void getRelationshipHashMapEdit_role1NotLetter_throwsParseException() {
        String userInput = "/1234 123 /1244 role /oldDescriptor /Newdescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void getRelationshipHashMapEditRole2NotLetter_throwsParseException() {
        String userInput = "/1234 /1244 123 /oldDescriptor /Newdescriptor";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
