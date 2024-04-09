package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.relationship.BioParentsRelationship;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;
import seedu.address.model.person.relationship.SiblingRelationship;
import seedu.address.model.person.relationship.SpousesRelationship;

public class JsonAdaptedRelationshipTest {

    private static final UUID VALID_UUID1 = UUID.randomUUID();
    private static final UUID VALID_UUID2 = UUID.randomUUID();
    private static final String VALID_DESCRIPTOR1 = "friend";
    private static final String VALID_DESCRIPTOR2 = "siblings";
    private static final String VALID_DESCRIPTOR3 = "spouses";
    private static final String VALID_DESCRIPTOR4 = "bioparents";
    private static final String VALID_ROLE1 = "parent";
    private static final String VALID_ROLE2 = "child";

    @Test
    public void toModelType_validRelationshipDetails_returnsRelationship() throws Exception {
        JsonAdaptedRelationship relationship =
                new JsonAdaptedRelationship(new Relationship(VALID_UUID1, VALID_UUID2, VALID_DESCRIPTOR1));
        assertEquals(new Relationship(VALID_UUID1, VALID_UUID2, VALID_DESCRIPTOR1), relationship.toModelType());
    }

    @Test
    public void toModelType_validBioParentsRelationshipDetails_returnsBioParentsRelationship() throws Exception {
        JsonAdaptedRelationship relationship = new JsonAdaptedRelationship(
                VALID_UUID1.toString(), VALID_UUID2.toString(), VALID_DESCRIPTOR4, VALID_ROLE1, VALID_ROLE2);
        assertEquals(
                new BioParentsRelationship(VALID_UUID1, VALID_UUID2, VALID_ROLE1, VALID_ROLE2),
                relationship.toModelType());
    }

    @Test
    public void toModelType_validSiblingRelationshipDetails_returnsSiblingRelationship() throws Exception {
        JsonAdaptedRelationship relationship = new JsonAdaptedRelationship(
                VALID_UUID1.toString(), VALID_UUID2.toString(), VALID_DESCRIPTOR2, "brother",
                "sister");
        assertEquals(
                new SiblingRelationship(VALID_UUID1, VALID_UUID2, "brother", "sister"),
                relationship.toModelType());
    }

    @Test
    public void toModelType_validSpousesRelationshipDetails_returnsSpousesRelationship() throws Exception {
        JsonAdaptedRelationship relationship = new JsonAdaptedRelationship(
                VALID_UUID1.toString(), VALID_UUID2.toString(), VALID_DESCRIPTOR3, "husband", "wife");
        assertEquals(
                new SpousesRelationship(VALID_UUID1, VALID_UUID2, "husband", "wife"),
                relationship.toModelType());
    }

    @Test
    public void toModelType_validRoleBasedRelationshipDetails_returnsRoleBasedRelationship() throws Exception {
        JsonAdaptedRelationship relationship = new JsonAdaptedRelationship(
                VALID_UUID1.toString(), VALID_UUID2.toString(), VALID_DESCRIPTOR1, VALID_ROLE1, VALID_ROLE2);
        assertEquals(
                new RoleBasedRelationship(VALID_UUID1, VALID_UUID2, VALID_DESCRIPTOR1, VALID_ROLE1, VALID_ROLE2),
                relationship.toModelType());
    }

    @Test
    public void toModelType_invalidDescriptor_throwsIllegalValueException() {
        JsonAdaptedRelationship relationship =
                new JsonAdaptedRelationship(VALID_UUID1.toString(),
                        VALID_UUID2.toString(), "", VALID_ROLE1, VALID_ROLE2);
        assertThrows(IllegalValueException.class, relationship::toModelType);
    }

    @Test
    public void toModelType_nullDescriptor_throwsIllegalValueException() {
        JsonAdaptedRelationship relationship =
                new JsonAdaptedRelationship(VALID_UUID1.toString(), VALID_UUID2.toString(),
                        null, VALID_ROLE1, VALID_ROLE2);
        assertThrows(IllegalValueException.class, relationship::toModelType);
    }
    @Test
    public void toModelType_nullPerson1_throwsIllegalValueException() {
        JsonAdaptedRelationship relationship =
                new JsonAdaptedRelationship(null, VALID_UUID2.toString(), VALID_DESCRIPTOR1, VALID_ROLE1, VALID_ROLE2);
        assertThrows(IllegalValueException.class, relationship::toModelType);
    }

    @Test
    public void toModelType_nullPerson2_throwsIllegalValueException() {
        JsonAdaptedRelationship relationship =
                new JsonAdaptedRelationship(VALID_UUID1.toString(), null, VALID_DESCRIPTOR1, VALID_ROLE1, VALID_ROLE2);
        assertThrows(IllegalValueException.class, relationship::toModelType);
    }
}
