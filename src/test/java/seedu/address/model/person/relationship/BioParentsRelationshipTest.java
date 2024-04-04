package seedu.address.model.person.relationship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class BioParentsRelationshipTest {

    @Test
    public void constructor_validParameters_success() {
        UUID originUuid = UUID.randomUUID();
        UUID targetUuid = UUID.randomUUID();
        String descriptor = "bioparents";
        String role1 = "parent";
        String role2 = "child";
        BioParentsRelationship relationship =
                new BioParentsRelationship(originUuid, targetUuid, role1, role2);
        assertEquals(originUuid, relationship.getPerson1());
        assertEquals(targetUuid, relationship.getPerson2());
        assertEquals(descriptor, relationship.getRelationshipDescriptor());
        assertEquals(role1, relationship.getRole(originUuid));
        assertEquals(role2, relationship.getRole(targetUuid));
    }

    @Test
    public void constructor_invalidUuid_throwsIllegalArgumentException() {
        String role1 = "child";
        String role2 = "parent";
        assertThrows(IllegalArgumentException.class, () ->
                new BioParentsRelationship(UUID.fromString("1"), UUID.fromString("2"), role1, role2));
    }

    @Test
    public void getStyleDescriptor_returnsBioparent_success() {
        UUID person1 = UUID.randomUUID();
        UUID person2 = UUID.randomUUID();

        BioParentsRelationship relationship = new BioParentsRelationship(person1, person2,
                "parent", "child");

        assertEquals("bioparent", relationship.getStyleDescriptor());
    }
}
