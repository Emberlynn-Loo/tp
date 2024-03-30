package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.relationship.Relationship;

public class RelationshipInRelationshipPathwayPredicateTest {

    @Test
    public void equals_sameRelationships_true() {
        // Prepare relationships
        ArrayList<Relationship> relationships1 = new ArrayList<>();
        relationships1.add(new Relationship(UUID.randomUUID(), UUID.randomUUID(), "parent"));

        ArrayList<Relationship> relationships2 = new ArrayList<>(relationships1); // Copy constructor

        // Prepare predicates
        RelationshipInRelationshipPathwayPredicate predicate1 = new RelationshipInRelationshipPathwayPredicate(relationships1);
        RelationshipInRelationshipPathwayPredicate predicate2 = new RelationshipInRelationshipPathwayPredicate(relationships2);

        // Test for equality
        assertTrue(predicate1.equals(predicate2));
    }

    @Test
    public void equals_differentRelationships_false() {
        // Prepare relationships
        ArrayList<Relationship> relationships1 = new ArrayList<>();
        relationships1.add(new Relationship(UUID.randomUUID(), UUID.randomUUID(), "parent"));

        ArrayList<Relationship> relationships2 = new ArrayList<>();
        relationships2.add(new Relationship(UUID.randomUUID(), UUID.randomUUID(), "sibling"));

        // Prepare predicates
        RelationshipInRelationshipPathwayPredicate predicate1 = new RelationshipInRelationshipPathwayPredicate(relationships1);
        RelationshipInRelationshipPathwayPredicate predicate2 = new RelationshipInRelationshipPathwayPredicate(relationships2);

        // Test for inequality
        assertFalse(predicate1.equals(predicate2));
    }

    @Test
    public void equals_sameObject_true() {
        // Prepare relationships
        ArrayList<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship(UUID.randomUUID(), UUID.randomUUID(), "parent"));

        // Prepare predicate
        RelationshipInRelationshipPathwayPredicate predicate = new RelationshipInRelationshipPathwayPredicate(relationships);

        // Test for equality with itself
        assertTrue(predicate.equals(predicate));
    }

    @Test
    public void equals_withNull_false() {
        // Prepare relationships
        ArrayList<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship(UUID.randomUUID(), UUID.randomUUID(), "parent"));

        // Prepare predicate
        RelationshipInRelationshipPathwayPredicate predicate = new RelationshipInRelationshipPathwayPredicate(relationships);

        // Test for inequality with null
        assertFalse(predicate.equals(null));
    }

    @Test
    public void equals_differentType_false() {
        // Prepare relationships
        ArrayList<Relationship> relationships = new ArrayList<>();
        relationships.add(new Relationship(UUID.randomUUID(), UUID.randomUUID(), "parent"));

        // Prepare predicate
        RelationshipInRelationshipPathwayPredicate predicate = new RelationshipInRelationshipPathwayPredicate(relationships);
        Object other = new Object(); // Different type

        // Test for inequality with different type object
        assertFalse(predicate.equals(other));
    }
}
