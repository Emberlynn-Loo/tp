package seedu.address.model.person.relationship;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class SiblingRelationshipTest {

    @Test
    public void constructor_validParameters_success() {
        UUID person1 = UUID.randomUUID();
        UUID person2 = UUID.randomUUID();

        SiblingRelationship relationship = new SiblingRelationship(person1, person2, "brother", "sister");

        assertEquals(person1, relationship.getPerson1());
        assertEquals(person2, relationship.getPerson2());
        assertEquals("brother", relationship.getRoleDescriptor(person1));
        assertEquals("sister", relationship.getRoleDescriptor(person2));
    }

    @Test
    public void getStyleDescriptor_returnsSibling_success() {
        UUID person1 = UUID.randomUUID();
        UUID person2 = UUID.randomUUID();

        SiblingRelationship relationship = new SiblingRelationship(person1, person2, "brother", "sister");

        assertEquals("sibling", relationship.getStyleDescriptor());
    }
    @Test
    public void testGetStyleDescriptor() {
        UUID person1 = UUID.randomUUID();
        UUID person2 = UUID.randomUUID();
        SiblingRelationship relationship = new SiblingRelationship(person1, person2, "brother", "sister");
        assertEquals("sibling", relationship.getStyleDescriptor());
    }
}
