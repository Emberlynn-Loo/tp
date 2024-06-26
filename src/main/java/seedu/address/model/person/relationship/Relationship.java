package seedu.address.model.person.relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Represents a relationship between two people.
 */
public class Relationship {
    protected static ArrayList<String> validDescriptors = new ArrayList<>(
            Arrays.asList("friends", "siblings", "spouses", "bioparents"));

    protected UUID person1;
    protected UUID person2;
    protected boolean isFamilyRelationship;
    protected String relationshipDescriptor;

    /**
     * Creates a new Relationship object with the given UUIDs.
     *
     * @param person1 The UUID of the first person in the relationship.
     * @param person2 The UUID of the second person in the relationship.
     */
    public Relationship(UUID person1, UUID person2, String relationshipDescriptor) {
        this.person1 = person1;
        this.person2 = person2;

        if (!validDescriptors.contains(relationshipDescriptor)) {
            if (!relationshipDescriptor.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*")) {
                throw new IllegalArgumentException("Invalid Relationship type. Must only consist of letters.");
            } else {
                validDescriptors.add(relationshipDescriptor);
            }
        }
        isFamilyRelationship = relationshipDescriptor.equalsIgnoreCase("family");
        this.relationshipDescriptor = relationshipDescriptor;
    }

    // Getters for person UUIDs
    public UUID getPerson1() {
        return person1;
    }

    public UUID getPerson2() {
        return person2;
    }
    public String getRelationshipDescriptor() {
        return this.relationshipDescriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Relationship)) {
            return false;
        }
        Relationship other = (Relationship) o;
        return ((other.person1.equals(this.person1) && other.person2.equals(this.person2))
                || (other.person1.equals(this.person2) && other.person2.equals(this.person1)))
                && other.relationshipDescriptor.equals(relationshipDescriptor);
    }
    public String getStyleDescriptor() {
        return "general";
    }

    /**
     * check if relationship has the UUID if there is return the other party so that BFS can be done
     * otherwise return null
     * @param origin
     * @return
     */
    public UUID containsUuid(UUID origin) {
        if (origin.equals(person1)) {
            return person2;
        }
        if (origin.equals(person2)) {
            return person1;
        }
        return null;
    }
    @Override
    public String toString() {
        return String.format("%s and %s are %s", person1.toString(),
                person2.toString(), this.relationshipDescriptor);
    }

    /**
     * suppose (abcd, edfg) is exisiting relationship of relationshipDescriptor friend, then
     * getRelativeRelationshipDescrptor(edfg) will return friend of abcd
     * @param origin
     * @return
     */
    public String getRelativeRelationshipDescriptor(UUID origin) {
        UUID target = origin.equals(this.person1) ? this.person2 : this.person1;
        String targetString = target.toString();
        String lastFourCharactersOfTargetString = targetString.substring(targetString.length() - 4);
        return String.format("%s of %s", relationshipDescriptor, lastFourCharactersOfTargetString);
    }
    /**
     * suppose (abcd, edfg) is exisiting relationship of relationshipDescriptor friend, then
     * getRelativeRelationshipDescrptor(edfg) will return friend of abcd
     * @param origin
     * @return
     */
    public String getRelativeRelationshipDescriptorWithoutUuid(UUID origin) {
        UUID target = origin.equals(this.person1) ? this.person2 : this.person1;
        return String.format("%s of", relationshipDescriptor);
    }

    /**
     * Adds a new relationship type to the list of valid relationship types.
     */
    public static String showRelationshipTypes() {
        return String.format("Valid relationship types are: %s", validDescriptors.toString());
    }
}
