package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class PersonInRelationshipPathwayPredicateTest {

    @Test
    public void equals_samePersonUuids_true() {
        // Prepare person UUIDs
        ArrayList<UUID> personUuids1 = new ArrayList<>();
        personUuids1.add(UUID.randomUUID());

        ArrayList<UUID> personUuids2 = new ArrayList<>(personUuids1); // Copy constructor

        // Prepare predicates
        PersonInRelationshipPathwayPredicate predicate1 = new PersonInRelationshipPathwayPredicate(personUuids1);
        PersonInRelationshipPathwayPredicate predicate2 = new PersonInRelationshipPathwayPredicate(personUuids2);

        // Test for equality
        assertTrue(predicate1.equals(predicate2));
    }

    @Test
    public void equals_differentPersonUuids_false() {
        // Prepare person UUIDs
        ArrayList<UUID> personUuids1 = new ArrayList<>();
        personUuids1.add(UUID.randomUUID());

        ArrayList<UUID> personUuids2 = new ArrayList<>();
        personUuids2.add(UUID.randomUUID()); // Different UUID

        // Prepare predicates
        PersonInRelationshipPathwayPredicate predicate1 = new PersonInRelationshipPathwayPredicate(personUuids1);
        PersonInRelationshipPathwayPredicate predicate2 = new PersonInRelationshipPathwayPredicate(personUuids2);

        // Test for inequality
        assertFalse(predicate1.equals(predicate2));
    }

    @Test
    public void equals_sameObject_true() {
        // Prepare person UUIDs
        ArrayList<UUID> personUuids = new ArrayList<>();
        personUuids.add(UUID.randomUUID());

        // Prepare predicate
        PersonInRelationshipPathwayPredicate predicate = new PersonInRelationshipPathwayPredicate(personUuids);

        // Test for equality with itself
        assertTrue(predicate.equals(predicate));
    }

    @Test
    public void equals_withNull_false() {
        // Prepare person UUIDs
        ArrayList<UUID> personUuids = new ArrayList<>();
        personUuids.add(UUID.randomUUID());

        // Prepare predicate
        PersonInRelationshipPathwayPredicate predicate = new PersonInRelationshipPathwayPredicate(personUuids);

        // Test for inequality with null
        assertFalse(predicate.equals(null));
    }

    @Test
    public void equals_differentType_false() {
        // Prepare person UUIDs
        ArrayList<UUID> personUuids = new ArrayList<>();
        personUuids.add(UUID.randomUUID());

        // Prepare predicate
        PersonInRelationshipPathwayPredicate predicate = new PersonInRelationshipPathwayPredicate(personUuids);
        Object other = new Object(); // Different type

        // Test for inequality with different type object
        assertFalse(predicate.equals(other));
    }
}
