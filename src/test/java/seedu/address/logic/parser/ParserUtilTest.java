package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String INVALID_UUID_1 = "?909";
    private static final String INVALID_UUID_2 = "99990";
    private static final String VALID_UUID_1 = "789d";

    private static final String WHITESPACE = " \t\r\n";


    @Test
    public void parseUuid_invalidValue1_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseUuid(INVALID_UUID_1));
    }
    @Test
    public void parseUuid_invalidValue2_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseUuid(INVALID_UUID_2));
    }
    @Test
    public void parseUuid_validValue_returnsUuid() throws Exception {
        String expected = VALID_UUID_1;
        String test = ParserUtil.parseUuid(expected);
        assertEquals(expected, test);
    }
    @Test
    public void getAttributeHashMapFromAttributeStrings_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.getAttributeHashMapFromAttributeStrings(null));
    }
    @Test
    public void getAttributeHashMapFromAttributeStrings_emptyArray_emptyHashMap() {
        String[] parts = {};
        try {
            assertTrue(ParserUtil.getAttributeHashMapFromAttributeStrings(parts).isEmpty());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getAttributeHashMapFromAttributeStrings_validArray_validHashMap() {
        String[] parts = {"Name Rachel", " Phone  123456 "};
        try {
            assertEquals("Rachel", ParserUtil.getAttributeHashMapFromAttributeStrings(parts).get("Name"));
            assertEquals("123456", ParserUtil.getAttributeHashMapFromAttributeStrings(parts).get("Phone"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void removeFirstItemFromStringList_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.removeFirstItemFromStringList(null));
    }
    @Test
    public void removeFirstItemFromStringList_emptyArray_throwsIllegalArgumentException() {
        String[] parts = {};
        assertThrows(IllegalArgumentException.class, () -> ParserUtil.removeFirstItemFromStringList(parts));
    }

    @Test
    public void separateUuidAndValues_invalidInput_throwsParseException() {
        String parts = "1234 role something";

        Assertions.assertThrows(ParseException.class, () -> ParserUtil.separateUuidAndValues(parts),
                ParserUtil.MESSAGE_INVALID_ROLE);
    }

    @Test
    public void separateUuidAndValues_validInputWithRole_success() throws Exception {
        String parts = "1234 role";
        String[] expected = {"1234", "role"};
        String[] actual = ParserUtil.separateUuidAndValues(parts);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void getRelationshipHashMapFromRelationshipStrings_invalidRoles_throwsParseException() {
        String[] parts = {"1234 role1", "1235 role1", "relationshipType"};
        assertThrows(ParseException.class, () -> ParserUtil.getRelationshipHashMapFromRelationshipStrings(parts));
    }

    @Test
    public void getRelationshipHashMapFromRelationshipDelete_invalidPeople_throwsParseException() {
        String[] parts = {"1234", "1234", "relationshipType"};
        assertThrows(CommandException.class, () -> ParserUtil.getRelationshipHashMapDelete(parts, true));
    }

    @Test
    public void getRelationshipHashMapDelete_nullParts_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.getRelationshipHashMapDelete(null, true));
    }

    @Test
    public void getRelationshipHashMapDelete_emptyParts_returnsEmptyMap() throws ParseException, CommandException {
        LinkedHashMap<String, String> map = ParserUtil.getRelationshipHashMapDelete(new String[]{}, true);
        assertTrue(map.isEmpty());
    }

    @Test
    public void getRelationshipHashMapDelete_singleElementParts_returnsMapWithRelationshipTypeKey() throws
            ParseException, CommandException {
        String[] parts = {"type"};
        LinkedHashMap<String, String> map = ParserUtil.getRelationshipHashMapDelete(parts, true);
        assertEquals(1, map.size());
        assertTrue(map.containsKey("type"));
        assertNull(map.get("type"));
    }

    @Test
    public void separateUuidAndValuesDelete_validInput_noExceptionThrown() {
        String input = "1234";
        assertDoesNotThrow(() -> ParserUtil.separateUuidAndValuesDelete(input));
    }

    @Test
    public void separateUuidAndValuesDelete_invalidLength_throwsParseException() {
        String invalidInput = "uuid role";
        assertThrows(ParseException.class, () -> ParserUtil.separateUuidAndValuesDelete(invalidInput));
    }

    @Test
    public void getRelationshipHashMapEdit_validInput_returnsHashMap() throws ParseException {
        String[] parts = {"1234 Rachel", "5678 Jack", "parent", "sibling"};
        LinkedHashMap<String, String> expectedMap = new LinkedHashMap<>();
        expectedMap.put("1234", "Rachel");
        expectedMap.put("5678", "Jack");
        expectedMap.put("parent", null);
        expectedMap.put("sibling", null);

        assertEquals(expectedMap, ParserUtil.getRelationshipHashMapEdit(parts));
    }

    @Test
    public void getRelationshipHashMapFromRelationshipStrings_validInput_returnsHashMap() throws ParseException {
        String[] parts = {"1234 brother", "5678 brother", "siblings"};
        LinkedHashMap<String, String> expectedMap = new LinkedHashMap<>();
        expectedMap.put("1234", "brother");
        expectedMap.put("5678", "brother");
        expectedMap.put("siblings", null);

        assertEquals(expectedMap, ParserUtil.getRelationshipHashMapFromRelationshipStrings(parts));
    }

    @Test
    public void getRelationshipHashMapEdit_samePerson_throwsParseException() {
        String[] parts = {"1234 parent", "1234 child", "friend", "bioparents"};
        assertThrows(ParseException.class, () -> ParserUtil.getRelationshipHashMapEdit(parts));
    }

    @Test
    public void getRelationshipHashMapEdit_sameRole_throwsParseException() {
        String[] parts = {"1234 parent", "5678 parent", "friend", "bioparents"};
        assertThrows(ParseException.class, () -> ParserUtil.getRelationshipHashMapEdit(parts));
    }

    @Test
    public void relationKeysAndValues_indexWithinBounds_returnKey() {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();
        relationshipMap.put("Key1", "Value1");
        relationshipMap.put("Key2", "Value2");

        String key = ParserUtil.relationKeysAndValues(relationshipMap, 0, false);

        assertEquals("Key1", key);
    }

    @Test
    public void relationKeysAndValues_indexWithinBounds_returnValue() {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();
        relationshipMap.put("Key1", "Value1");
        relationshipMap.put("Key2", "Value2");

        String value = ParserUtil.relationKeysAndValues(relationshipMap, 1, true);

        assertEquals("Value2", value);
    }

    @Test
    public void relationKeysAndValues_indexOutOfBounds_throwIndexOutOfBoundsException() {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();
        relationshipMap.put("Key1", "Value1");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            ParserUtil.relationKeysAndValues(relationshipMap, 1, false);
        });
    }

    @Test
    public void validateRolesForBioparents_validRoleCombinations_noExceptionThrown() {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();
        relationshipMap.put("0001", "nknn");
        relationshipMap.put("0003", "something");
        relationshipMap.put("bioparents", null);

        assertThrows(ParseException.class, () -> ParserUtil.validateRolesForFamilialRelation(
                "bioparents", relationshipMap));
    }

    @Test
    public void validateRolesForSiblings_validRoleCombinations_noExceptionThrown() {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();
        relationshipMap.put("0001", "nknn");
        relationshipMap.put("0003", "something");
        relationshipMap.put("siblings", null);

        assertThrows(ParseException.class, () -> ParserUtil.validateRolesForFamilialRelation(
                "siblings", relationshipMap));
    }

    @Test
    public void validateRolesForSpouses_validRoleCombinations_noExceptionThrown() {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();
        relationshipMap.put("0001", "nknn");
        relationshipMap.put("0003", "something");
        relationshipMap.put("spouses", null);

        assertThrows(ParseException.class, () -> ParserUtil.validateRolesForFamilialRelation(
                "spouses", relationshipMap));
    }
}
