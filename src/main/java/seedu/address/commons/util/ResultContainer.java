package seedu.address.commons.util;

import java.util.ArrayList;
import java.util.UUID;
import seedu.address.model.person.relationship.Relationship;

/**
 * A container class to hold the results of a relationship search.
 * This includes a list of UUIDs representing the persons involved,
 * a list of relationships found between these persons,
 * and a string describing the pathway of these relationships.
 */
public class ResultContainer {
    private ArrayList<UUID> personsUuid;
    private ArrayList<Relationship> relationships;
    private String relationshipPathway;

    /**
     * Constructs a {@code ResultContainer} with the specified lists of persons' UUIDs,
     * relationships among these persons, and a textual description of the relationship pathway.
     *
     * @param personsUuid An {@code ArrayList} of UUIDs for the persons involved.
     * @param relationships An {@code ArrayList} of {@code Relationship} objects representing
     *                      the relationships found between the persons.
     * @param relationshipPathway A {@code String} representation of the pathway
     *                            through the relationships.
     */
    public ResultContainer(ArrayList<UUID> personsUuid,
                           ArrayList<Relationship> relationships, String relationshipPathway) {
        this.personsUuid = personsUuid;
        this.relationships = relationships;
        this.relationshipPathway = relationshipPathway;
    }

    /**
     * Returns the list of UUIDs for the persons involved in the relationships.
     *
     * @return An {@code ArrayList} of UUIDs representing the persons.
     */
    public ArrayList<UUID> getPersons() {
        return personsUuid;
    }

    /**
     * Returns the list of relationships found between the persons.
     *
     * @return An {@code ArrayList} of {@code Relationship} objects.
     */
    public ArrayList<Relationship> getRelationships() {
        return relationships;
    }

    /**
     * Returns the string representation of the pathway through the relationships.
     *
     * @return A {@code String} describing the relationship pathway.
     */
    public String getRelationshipPathway() {
        return relationshipPathway;
    }
}
