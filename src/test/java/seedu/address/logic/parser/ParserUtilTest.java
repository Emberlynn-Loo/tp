package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

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
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName(null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone(null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress(null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail(null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
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
}
