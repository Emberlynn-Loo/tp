package seedu.address.model.person;

import java.util.ArrayList;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.relationship.Relationship;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class RelationshipInRelationshipPathwayPredicate implements Predicate<Relationship> {
    private final ArrayList<Relationship> relationships;

    public RelationshipInRelationshipPathwayPredicate(ArrayList<Relationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public boolean test(Relationship relationship) {
        return relationships.stream().anyMatch(r ->
                relationship.equals(r));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RelationshipInRelationshipPathwayPredicate)) {
            return false;
        }

        RelationshipInRelationshipPathwayPredicate otherRelationshipInRelationshipPathwayPredicate =
                (RelationshipInRelationshipPathwayPredicate) other;
        return relationships.equals(otherRelationshipInRelationshipPathwayPredicate.relationships);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", relationships).toString();
    }
}
