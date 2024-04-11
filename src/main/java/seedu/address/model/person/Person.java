package seedu.address.model.person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.attribute.*;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    private final UUID uuid;
    // Data fields
    private final TreeMap<String, Attribute> attributes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Constructs a person with a random UUID and a list of attributes.
     * There are no compulsory fields, attribute list can be null.
     *
     * @params attributes A list of attributes to be added to the person.
     *      If the list is null, the person will have no attributes.
     *      If there are multiple attributes of the same type, the last one will be used.
     * @return A person with the given attributes.
     */
    public Person(Attribute[] attributes) {
        this.uuid = UUID.randomUUID();
        for (Attribute attribute : attributes) {
            this.attributes.put(attribute.getName(), attribute);
        }
    }

    /**
     * Constructs a person with a given UUID and a list of attributes.
     * There are no compulsory fields, attribute list can be null.
     *
     * @params uuid The UUID of the person.
     * @params attributes A list of attributes to be added to the person.
     *      If the list is null, the person will have no attributes.
     *      If there are multiple attributes of the same type, the last one will be used.
     * @return A person with the given attributes.
     */
    public Person(UUID uuid, Attribute[] attributes) {
        this.uuid = uuid;
        for (Attribute attribute : attributes) {
            this.attributes.put(attribute.getName(), attribute);
        }
    }

    /**
     * Constructs a person with a random UUID and a list of attributes.
     * There are no compulsory fields, attribute list can be null.
     *
     * @params attributes A list of attributes to be added to the person.
     *      If the list is null, the person will have no attributes.
     *      If there are multiple attributes of the same type, the last one will be used.
     * @return A person with the given attributes.
     */
    public Person(NameAttribute name, PhoneNumberAttribute phone, StringAttribute email, StringAttribute address) {
        this.uuid = UUID.randomUUID();
        this.attributes.put("Name", name);
        this.attributes.put("Phone", phone);
        this.attributes.put("Email", email);
        this.attributes.put("Address", address);
    }

    /**
     * Constructs a person with a given UUID and a list of attributes.
     * There are no compulsory fields, attribute list can be null.
     *
     * @params fromString The UUID of the person.
     * @params name The name of the person.
     * @params phone The phone number of the person.
     * @params email The email of the person.
     * @params address The address of the person.
     * @return A person with the given attributes.
     */
    public Person(UUID fromString, NameAttribute name, PhoneNumberAttribute phone,
                  StringAttribute email, StringAttribute address) {
        this.uuid = fromString;
        this.attributes.put("Name", name);
        this.attributes.put("Phone", phone);
        this.attributes.put("Email", email);
        this.attributes.put("Address", address);
    }

    /**
     * Returns the uuid of the person.
     *
     * @return The uuid of the person.
     */
    public UUID getUuid() {
        return uuid;
    }
    /**
     * Returns the uuid of the person as a string.
     *
     * @return The uuid of the person as a string.
     */
    public String getUuidString() {
        return uuid.toString();
    }

    /**
     * Return the last four character of person uuid
     * @return last four character of person uuid
     */
    public String getLastFourCharacterOfUuid() {
        String uuid = getUuidString();
        int len = uuid.length();
        return uuid.substring(len - 4);
    }
    /**
     * Returns whether the person has an attribute with the given type.
     *
     * @param attributeName The type of the attribute to check for.
     * @return True if the person has an attribute with the given type.
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.containsKey(attributeName);
    }
    /**
     * Returns an attribute of the person.
     *
     * @param attributeName The type of the attribute to get.
     * @return The attribute with the given type.
     */
    public Attribute getAttribute(String attributeName) {
        assertValidAttributeName(attributeName);
        assertAttributeExistsInPerson(attributeName, attributes);
        return attributes.get(attributeName);
    }
    /**
     * Edits/adds an attribute of/to the person.
     *
     * @param attribute The attribute to edit or add.
     */
    public void updateAttribute(Attribute attribute) {
        assertValidAttribute(attribute);
        attributes.put(attribute.getName(), attribute);
    }

    /**
     * Deletes an attribute from the person.
     *
     * @param attributeType The type of the attribute to delete.
     * @return
     */
    public void deleteAttribute(String attributeType) {
        if (attributes.containsKey(attributeType)) {
            attributes.remove(attributeType);
        }
    }

    public void genderMatch(String gender, String uuid) {
        String genderRole;
        if (gender.equalsIgnoreCase("brother") || gender.equalsIgnoreCase("husband")) {
            genderRole = "MALE";
        } else {
            genderRole = "FEMALE";
        }
        String attributeGender = attributes.get("Sex").getValueAsString();
        if (!attributeGender.equals(genderRole)) {
            throw new IllegalArgumentException("Sex attribute of " + uuid + " does not match the gender of your inputted "
                    + "role.\nIf you'd like to change the gender of the person, please change the sex attribute by "
                    + "deleting and re-adding it as the gender you want.");
        }
    }


    static void assertValidAttribute(Attribute attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute cannot be null.");
        }
        assertValidAttributeName(attribute.getName());
    }

    private static void assertValidAttributeName(String attributeType) {
        if (attributeType == "") {
            throw new IllegalArgumentException("Attribute name cannot be empty.");
        }
    }

    private static void assertAttributeExistsInPerson(String attributeType, Map<String, Attribute> attributes) {
        if (!attributes.containsKey(attributeType)) {
            throw new IllegalArgumentException("Attribute with name " + attributeType + " does not exist");
        }
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }
        Person otherPerson = (Person) other;

        return otherPerson.getUuid().equals(this.getUuid());
    }
    /**
     * Returns true if the UUID of the person is the same as the UUID of the other object.
     *
     * @param other The object to compare with.
     * @return True if the UUID of the person is the same as the UUID of the other object.
     */
    public boolean equalsUuid(Object other) {
        if (!(other instanceof Person)) {
            return false;
        }

        if (other == this) {
            return true;
        }

        Person otherPerson = (Person) other;
        return uuid.equals(otherPerson.uuid);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(
                attributes,
                uuid);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("uuid", uuid)
                .toString();
    }
    /**
     * Returns a string representation of all the attributes of the person.
     * The attributes are sorted by length of the string representation.
     *
     * @return A string representation of all the attributes of the person.
     */
    public String allAttributesAsString() {
        StringBuilder sb = new StringBuilder();
        if (attributes.isEmpty()) {
            return "No details available";
        }
        // Solution below generated by Github Copilot
        List<Map.Entry<String, Attribute>> sortedAttributes = new ArrayList<>(attributes.entrySet());
        sortedAttributes.sort(
                Comparator.comparingInt(
                        entry -> (entry.getKey() + ": " + entry.getValue().getValueAsString()).length()));

        for (Map.Entry<String, Attribute> entry : sortedAttributes) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue().getValueAsString()).append("\n");
        }
        return sb.toString().trim();
    }
    /**
     * Sorts the keys in the attribute Map in lexicographical order, after the function will convert the Map into
     * an array of array of Strings of size 2, where the first element is the descriptor of the key and the second
     * element of each element String array is the String of the attribute value
     * @return
     */
    public String[][] allAttributesAsPairs() {
        if (attributes.isEmpty()) {
            return null;
        }
        // sorts by lexicographical ordering of attributes key as compared to the method above sorts by the combined
        // length of key and value leading to inconsistent ordering in a personCard
        List<Map.Entry<String, Attribute>> sortedAttributes = new ArrayList<>(attributes.entrySet());
        sortedAttributes.sort(
                Comparator.comparing(
                        Map.Entry::getKey));
        String[][] result = new String[sortedAttributes.size()][2];
        for (int i = 0; i < sortedAttributes.size(); i++) {
            Map.Entry<String, Attribute> entry = sortedAttributes.get(i);
            String[] current = new String[] {String.format("%s:", entry.getKey()), entry.getValue().getValueAsString()};
            result[i] = current;
        }
        return result;
    }

    public Set<Attribute> getAttributes() {
        return new HashSet<>(attributes.values());
    }
    public TreeMap<String, Attribute> getAttributesMap() {
        return this.attributes;
    }
    public void setAttribute(String name, String str) {
        attributes.put(name, new StringAttribute(name, str));
    }
}
