package seedu.address.model.person.relationship;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleBasedRelationshipTest {

    @Test
    public void constructor_validParameters_success() {
        UUID originUuid = UUID.randomUUID();
        UUID targetUuid = UUID.randomUUID();
        String descriptor = "work";
        String role1 = "boss";
        String role2 = "employee";
        RoleBasedRelationship relationship =
                new RoleBasedRelationship(originUuid, targetUuid, descriptor, role1, role2);
        assertEquals(originUuid, relationship.getPerson1());
        assertEquals(targetUuid, relationship.getPerson2());
        assertEquals(descriptor, relationship.getRelationshipDescriptor());
        assertEquals(role1, relationship.getRole(originUuid));
        assertEquals(role2, relationship.getRole(targetUuid));
    }

    @Test
    public void constructor_invalidParameters_throwsIllegalArgumentException() {
        String descriptor = "work";
        String role1 = "boss";
        String role2 = "employee";
        assertThrows(IllegalArgumentException.class, () ->
                new RoleBasedRelationship(UUID.fromString("1"), UUID.fromString("2"),
                        descriptor, role1, role2));
    }
}