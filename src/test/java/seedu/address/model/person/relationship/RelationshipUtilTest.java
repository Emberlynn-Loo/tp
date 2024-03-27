package seedu.address.model.person.relationship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.person.attribute.Attribute;
import seedu.address.model.person.attribute.NameAttribute;

public class RelationshipUtilTest {

    @Test
    public void deleteRelationship_existingRelationship_successfullyDeleted() {
        Attribute name1 = new NameAttribute("Name", "John Doe");
        Attribute name2 = new NameAttribute("Name", "Jane Doe");
        Attribute[] attributes1 = new Attribute[]{name1};
        Attribute[] attributes2 = new Attribute[]{name2};

        // Adding dummy people for testing
        Person person1 = new Person(attributes1);
        Person person2 = new Person(attributes2);
        UUID uuid11 = person1.getUuid();
        UUID uuid22 = person2.getUuid();
        // Create a test relationship
        Relationship testRelationship = new Relationship(uuid11, uuid22, "family");

        // Create RelationshipUtil instance and add the test relationship
        RelationshipUtil relationshipUtil = new RelationshipUtil();
        relationshipUtil.addRelationship(testRelationship);

        // Delete the test relationship
        relationshipUtil.deleteRelationship(testRelationship);

        // Check if the relationship has been deleted
        assertFalse(relationshipUtil.hasRelationship(testRelationship));
    }

    @Test
    public void hasRelationship_existingRelationship_returnsTrue() {
        Attribute name1 = new NameAttribute("Name", "John Doe");
        Attribute name2 = new NameAttribute("Name", "Jane Doe");
        Attribute[] attributes1 = new Attribute[]{name1};
        Attribute[] attributes2 = new Attribute[]{name2};

        // Adding dummy people for testing
        Person person1 = new Person(attributes1);
        Person person2 = new Person(attributes2);
        UUID uuid11 = person1.getUuid();
        UUID uuid22 = person2.getUuid();
        // Create a test relationship
        Relationship testRelationship = new Relationship(uuid11, uuid22, "family");

        // Create RelationshipUtil instance and add the test relationship
        RelationshipUtil relationshipUtil = new RelationshipUtil();
        relationshipUtil.addRelationship(testRelationship);

        // Check if the relationship exists
        assertTrue(relationshipUtil.hasRelationship(testRelationship));
    }

    @Test
    public void hasRelationshipWithDescriptor_existingRelationshipWithDescriptor_returnsTrue() {
        // Create RelationshipUtil instance
        RelationshipUtil relationshipUtil = new RelationshipUtil();
        Relationship relationship = new Relationship(UUID.randomUUID(), UUID.randomUUID(), "housemates");
        relationshipUtil.addRelationship(relationship);
        assertTrue(relationshipUtil.descriptorExists("housemates"));
    }

    @Test
    public void descriptorExists_nonExistingDescriptor_returnsFalse() {
        // Create RelationshipUtil instance
        RelationshipUtil relationshipUtil = new RelationshipUtil();

        // Create a relationship with a different descriptor
        Relationship relationship = new Relationship(UUID.randomUUID(), UUID.randomUUID(), "friend");

        // Add the relationship to the RelationshipUtil
        relationshipUtil.addRelationship(relationship);

        // Check if the descriptor exists
        assertFalse(relationshipUtil.descriptorExists("family"));
    }

    @Test
    public void descriptorExists_noRelationships_returnsFalse() {
        // Create an empty RelationshipUtil instance
        RelationshipUtil relationshipUtil = new RelationshipUtil();

        // Check if the descriptor exists when there are no relationships
        assertFalse(relationshipUtil.descriptorExists("family"));
    }

    @Test
    public void hasRelationshipWithDescriptor_matchingRelationship_returnsTrue() {
        // Create dummy people for testing
        Attribute name1 = new NameAttribute("Name", "John Doe");
        Attribute name2 = new NameAttribute("Name", "Jane Doe");
        Attribute[] attributes1 = new Attribute[]{name1};
        Attribute[] attributes2 = new Attribute[]{name2};
        Person person1 = new Person(attributes1);
        Person person2 = new Person(attributes2);
        UUID uuid1 = person1.getUuid();
        UUID uuid2 = person2.getUuid();

        // Create two test relationships
        Relationship matchingRelationship = new Relationship(uuid1, uuid2, "family");
        Relationship nonMatchingRelationship = new Relationship(uuid1, uuid2, "friend");

        // Create RelationshipUtil instance and add the relationships
        RelationshipUtil relationshipUtil = new RelationshipUtil();
        relationshipUtil.addRelationship(matchingRelationship);
        relationshipUtil.addRelationship(nonMatchingRelationship);

        // Check if the matching relationship exists with the correct descriptor
        assertTrue(relationshipUtil.hasRelationshipWithDescriptor(matchingRelationship));
    }

    @Test
    public void hasRelationshipWithDescriptor_existingRelationshipWithReversedPersons_returnsTrue() {
        // Create dummy people for testing
        Attribute name1 = new NameAttribute("Name", "John Doe");
        Attribute name2 = new NameAttribute("Name", "Jane Doe");
        Attribute[] attributes1 = new Attribute[]{name1};
        Attribute[] attributes2 = new Attribute[]{name2};
        Person person1 = new Person(attributes1);
        Person person2 = new Person(attributes2);
        UUID uuid1 = person1.getUuid();
        UUID uuid2 = person2.getUuid();

        // Create a test relationship with reversed persons
        Relationship testRelationship = new Relationship(uuid2, uuid1, "family");

        // Create RelationshipUtil instance and add the test relationship
        RelationshipUtil relationshipUtil = new RelationshipUtil();
        relationshipUtil.addRelationship(new Relationship(uuid1, uuid2, "family"));

        // Check if the relationship exists with reversed persons but the same role descriptor
        assertTrue(relationshipUtil.hasRelationshipWithDescriptor(testRelationship));
    }

    @Test
    public void getExistingRelationship_existingRelationship_returnsStringRepresentation() {
        // Create a RelationshipUtil instance
        RelationshipUtil relationshipUtil = new RelationshipUtil();

        // Create UUIDs for two persons
        UUID person1Uuid = UUID.randomUUID();
        UUID person2Uuid = UUID.randomUUID();

        // Create a relationship and add it to the RelationshipUtil
        Relationship relationship = new Relationship(person1Uuid, person2Uuid, "family");
        relationshipUtil.addRelationship(relationship);

        // Retrieve the string representation using getExistingRelationship
        String expectedString = String.format("%s and %s are %s",
                person1Uuid, person2Uuid, "family");
        String actualString = relationshipUtil.getExistingRelationship(relationship);

        // Assert that the retrieved string matches the expected string representation
        assertEquals(expectedString, actualString);
    }

    @Test
    public void getExistingRelationship_nonExistingRelationship_throwsIllegalArgumentException() {
        // Create a RelationshipUtil instance
        RelationshipUtil relationshipUtil = new RelationshipUtil();

        // Create UUIDs for two persons
        UUID person1Uuid = UUID.randomUUID();
        UUID person2Uuid = UUID.randomUUID();

        // Create a relationship without adding it to the RelationshipUtil
        Relationship relationship = new Relationship(person1Uuid, person2Uuid, "family");

        // Assert that trying to retrieve a non-existing relationship throws an exception
        assertThrows(IllegalArgumentException.class, () -> relationshipUtil.getExistingRelationship(relationship));
    }
    @Test
    public void testEqualsMethodWithSameInstance() {
        RelationshipUtil test = new RelationshipUtil();
        assertEquals(test.equals(test), true);
    }
    @Test
    public void testEqualsMethodReturnFalseWithDifferentInstance() {
        RelationshipUtil test = new RelationshipUtil();
        String test2 = "";
        assertEquals(test.equals(test2), false);
    }

    @Test
    public void deleteRelationshipsOfPerson_existingRelationships_relationshipsDeleted() {
        // Create dummy people for testing
        Attribute name1 = new NameAttribute("Name", "John Doe");
        Attribute name2 = new NameAttribute("Name", "Jane Doe");
        Attribute name3 = new NameAttribute("Name", "Jim Doe");
        Attribute[] attributes1 = new Attribute[]{name1};
        Attribute[] attributes2 = new Attribute[]{name2};
        Attribute[] attributes3 = new Attribute[]{name3};
        Person person1 = new Person(attributes1);
        Person person2 = new Person(attributes2);
        Person person3 = new Person(attributes3);
        UUID uuid1 = person1.getUuid();
        UUID uuid2 = person2.getUuid();
        UUID uuid3 = person3.getUuid();

        // Create relationships involving person1
        Relationship relationship1 = new Relationship(uuid1, uuid2, "family");
        Relationship relationship2 = new Relationship(uuid1, uuid3, "friend");
        Relationship relationship3 = new Relationship(uuid2, uuid1, "friend");

        // Create RelationshipUtil instance and add the relationships
        RelationshipUtil relationshipUtil = new RelationshipUtil();
        relationshipUtil.addRelationship(relationship1);
        relationshipUtil.addRelationship(relationship2);
        relationshipUtil.addRelationship(relationship3);

        // Delete all relationships involving person1
        relationshipUtil.deleteRelationshipsOfPerson(uuid1);

        // Check if the relationships involving person1 have been deleted
        assertFalse(relationshipUtil.hasRelationship(relationship1));
        assertFalse(relationshipUtil.hasRelationship(relationship2));
        assertFalse(relationshipUtil.hasRelationship(relationship3));

        // Create relationships involving person2
        // Create relationships involving person1 and person2
        Relationship relationship4 = new Relationship(uuid1, uuid2, "family");
        Relationship relationship5 = new Relationship(uuid2, uuid1, "friend");

        // Create RelationshipUtil instance and add the relationships
        RelationshipUtil relationshipUtil2 = new RelationshipUtil();
        relationshipUtil2.addRelationship(relationship1);
        relationshipUtil2.addRelationship(relationship2);

        // Delete all relationships involving person2
        relationshipUtil2.deleteRelationshipsOfPerson(uuid2);

        // Check if the relationships involving person2 have been deleted
        assertFalse(relationshipUtil2.hasRelationship(relationship4));
        assertFalse(relationshipUtil2.hasRelationship(relationship5));
    }
}
