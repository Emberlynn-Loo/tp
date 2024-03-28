package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_MALFORMED_ATTRIBUTE_PAIR = "Attributes must be of the format: "
            + "/attributeName value";

    public static final String MESSAGE_INVALID_RELATIONSHIP_FORMAT = "Relationship format is invalid. "
            + "\nPlease ensure that the relationship is in the format: "
            + "\naddRelation /<UUID1> /<UUID2> /<relationshipDescriptor> or "
            + "\naddRelation /<UUID1> <role1> /<UUID2> <role2> /<relationshipDescriptor>";

    public static final String MESSAGE_INVALID_ROLE = "Roles must be all strings and one word only";

    public static final String MESSAGE_INVALID_ROLE_DELETE = "Roles does not need to be specified for delete command";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a string into a string with 4 characters and whitespaces removed
     * @param uuid last 4 characters of a UUID
     * @return a trimmed String if uuid given as arguments is valid
     * @throws ParseException
     */
    public static String parseUuid(String uuid) throws ParseException {
        String trimmedUuid = uuid.trim();
        if (!StringUtil.isValidLastFourDigitsUuid(trimmedUuid)) {
            throw new ParseException(Messages.MESSAGE_INVALID_PERSON_UUID);
        }
        return trimmedUuid;
    }

    /**
     * Parses attributes in command arguments into a HashMap representing the pairs of attribute names and values
     *
     * @param parts The pairs of attribute names and values in the command arguments
     * @return  A HashMap containing the pairs of attribute names and values
     */
    public static HashMap<String, String> getAttributeHashMapFromAttributeStrings(String[] parts)
            throws ParseException {
        requireValidParts(parts);
        HashMap<String, String> attributeMap = new HashMap<>();
        for (int i = 0; i < parts.length; i++) {
            String[] attribute = separateAttributeNamesAndValues(parts[i]);
            String attributeName = attribute[0];
            String attributeValue = attribute[1];
            attributeMap.put(attributeName, attributeValue);
        }
        return attributeMap;
    }
    /**
     * Removes the first item from a string list
     *
     * @param parts The string list
     * @return A new string list with the first item removed
     */
    public static String[] removeFirstItemFromStringList(String[] parts) {
        if (parts.length == 0) {
            throw new IllegalArgumentException("The parts array should not be empty.");
        }
        // Solution below generated by GitHub Copilot
        String[] newParts;
        newParts = new String[parts.length - 1];
        System.arraycopy(parts, 1, newParts, 0, parts.length - 1);
        return newParts;
    }

    private static String[] separateAttributeNamesAndValues(String parts) throws ParseException {
        String[] result = parts.trim().split(" ", 2);
        if (result.length != 2) {
            throw new ParseException(String.format(MESSAGE_MALFORMED_ATTRIBUTE_PAIR));
        }
        result[0] = result[0].trim();
        result[1] = result[1].trim();
        return result;
    }

    private static String[] separateUuidAndValues(String parts) throws ParseException {
        String[] result = parts.trim().split(" ");
        if (result.length == 1) {
            result[0] = result[0].trim();
            return result;
        } else if (result.length == 2) {
            if (!result[1].matches("[a-zA-Z]+")) {
                throw new ParseException(String.format(MESSAGE_INVALID_ROLE));
            }
            result[0] = result[0].trim();
            result[1] = result[1].trim();
            return result;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_ROLE));
        }
    }

    private static String[] separateRelationshipTypes(String parts) throws ParseException {
        String[] result = parts.trim().split(" ", 1);
        if (result.length < 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_RELATIONSHIP_FORMAT));
        }
        result[0] = result[0].trim();
        return result;
    }

    private static void requireValidParts(String[] parts) throws ParseException {
        for (int i = 0; i < parts.length; i++) {
            separateAttributeNamesAndValues(parts[i]);
        }
    }

    /**
     * Parses relationships in command arguments into a LinkedHashMap representing the pairs of UUIDs and values
     *
     * @param parts The pairs of UUIDs and values in the command arguments
     * @return  A LinkedHashMap containing the pairs of UUIDs and values
     */
    public static LinkedHashMap<String, String> getRelationshipHashMapFromRelationshipStrings(String[] parts)
            throws ParseException {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();

        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                String[] uuidAndValue = separateUuidAndValues(parts[i]);
                String uuid = uuidAndValue[0];
                String value;
                if (uuidAndValue.length == 1) {
                    value = null;
                } else {
                    value = uuidAndValue[1];
                }
                relationshipMap.put(uuid, value);
            } else if (i == 1) {
                String[] uuidAndValue = separateUuidAndValues(parts[i]);
                String value = relationshipMap.keySet().toArray(new String[0])[0];
                if (uuidAndValue[0].equals(value)) {
                    throw new ParseException("Relationships must be between 2 different people");
                }
                String uuid = uuidAndValue[0];
                String value2;
                if (uuidAndValue.length == 1) {
                    value2 = null;
                } else {
                    if (uuidAndValue[1].equals(relationshipMap.values().toArray(new String[0])[0])) {
                        throw new ParseException("Roles must be different for each person in a relationship.");
                    }
                    value2 = uuidAndValue[1];
                }
                relationshipMap.put(uuid, value2);
            } else if (i == 2) {
                String[] relationshipType = separateRelationshipTypes(parts[i]);
                String relationshipTypeKey = relationshipType[0];
                relationshipMap.put(relationshipTypeKey, null);
            }
        }
        return relationshipMap;
    }

    /**
     * Parses relationships in command arguments into a LinkedHashMap representing the pairs of UUIDs and values
     *
     * @param parts The pairs of UUIDs and values in the command arguments
     * @return  A LinkedHashMap containing the pairs of UUIDs and values
     */
    public static LinkedHashMap<String, String> getRelationshipHashMapDelete(String[] parts, boolean hasUuids)
            throws ParseException, CommandException {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();

        if (hasUuids) {
            for (int i = 0; i < parts.length; i++) {
                if (i == 0) {
                    String[] uuidAndValue = separateUuidAndValuesDelete(parts[i]);
                    relationshipMap.put(uuidAndValue[0], null);
                } else if (i == 1) {
                    String[] uuidAndValue = separateUuidAndValuesDelete(parts[i]);
                    String value = relationshipMap.keySet().toArray(new String[0])[0];
                    if (uuidAndValue[0].equals(value)) {
                        throw new CommandException("Relationships must be between 2 different people");
                    }
                    relationshipMap.put(uuidAndValue[0], null);
                } else if (i == 2) {
                    String[] relationshipType = separateRelationshipTypes(parts[i]);
                    String relationshipTypeKey = relationshipType[0];
                    relationshipMap.put(relationshipTypeKey, null);
                }
            }
        } else {
            String[] relationshipType = separateRelationshipTypes(parts[0]);
            String relationshipTypeKey = relationshipType[0];
            relationshipMap.put(relationshipTypeKey, null);
        }
        return relationshipMap;
    }

    private static String[] separateUuidAndValuesDelete(String parts) throws ParseException {
        String[] result = parts.trim().split(" ");
        if (result.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_ROLE_DELETE));
        } else {
            result[0] = result[0].trim();
            return result;
        }
    }

    public static LinkedHashMap<String, String> getRelationshipHashMapEdit(String[] parts)
            throws ParseException {
        LinkedHashMap<String, String> relationshipMap = new LinkedHashMap<>();

        for (int i = 0; i < parts.length; i++) {
            String value;
            if (i == 0) {
                String uuid = separateUuidAndValues(parts[i])[0];
                if (separateUuidAndValues(parts[i]).length == 1) {
                    value = null;
                } else {
                    value = separateUuidAndValues(parts[i])[1];
                }
                relationshipMap.put(uuid, value);
            } else if (i == 1) {
                if (separateUuidAndValues(parts[i])[0].equals(relationshipMap.keySet().toArray(new String[0])[0])) {
                    throw new ParseException("Relationships must be between 2 different people");
                }
                String uuid = separateUuidAndValues(parts[i])[0];
                if (separateUuidAndValues(parts[i]).length == 1) {
                    value = null;
                } else {
                    if (separateUuidAndValues(parts[i])[1].equals(relationshipMap.values().toArray(new String[0])[0])) {
                        throw new ParseException("Roles must be different for each person in a relationship.");
                    }
                    value = separateUuidAndValues(parts[i])[1];
                }
                relationshipMap.put(uuid, value);
            } else if (i == 2) {
                String[] relationshipType = separateRelationshipTypes(parts[i]);
                String relationshipTypeKey = relationshipType[0];
                relationshipMap.put(relationshipTypeKey, null);
            } else if (i == 3) {
                String[] relationshipType = separateRelationshipTypes(parts[i]);
                if (separateUuidAndValues(parts[i])[0].equals(relationshipMap.keySet().toArray(new String[0])[0])) {
                    throw new ParseException("Relationship types must be different.");
                }
                String relationshipTypeKey = relationshipType[0];
                relationshipMap.put(relationshipTypeKey, null);
            }
        }
        return relationshipMap;
    }
}
