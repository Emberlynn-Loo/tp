package seedu.address.model.person.relationship;

import java.util.UUID;

/**
 * Represents a parent-child relationship between two persons.
 */
public class BioParentsRelationship extends FamilyRelationship {

    /**
     * Creates a new BioParentsRelationship with the given UUIDs of the two persons.
     *
     * @param parentUuid The UUID of the parent in the relationship.
     * @param childUuid The UUID of the child in the relationship.
     */
    public BioParentsRelationship(UUID parentUuid, UUID childUuid, String role1, String role2) {
        super(parentUuid, childUuid, "bioparents", role1, role2);
    }
    @Override
    public String getStyleDescriptor() {
        return "bioparent";
    }
}
