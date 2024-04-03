package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;

public class JsonAdaptedRelationshipDescriptorsTest {

    @Test
    public void toModelType_validRelationshipDescriptors_returnsRelationshipDescriptors() throws Exception {
        ArrayList<String> rolelessDescriptors = new ArrayList<>(Arrays.asList("friend", "colleagues"));
        ArrayList<ArrayList<String>> roleBasedDescriptors = new ArrayList<>();
        roleBasedDescriptors.add(new ArrayList<>(Arrays.asList("bioparents", "parent", "child")));
        roleBasedDescriptors.add(new ArrayList<>(Arrays.asList("spouses", "husband", "wife")));

        JsonAdaptedRelationshipDescriptors jsonAdaptedRelationshipDescriptors =
                new JsonAdaptedRelationshipDescriptors(rolelessDescriptors, roleBasedDescriptors);
        assertEquals(rolelessDescriptors, jsonAdaptedRelationshipDescriptors.toModelType().getKey());
        assertEquals(roleBasedDescriptors, jsonAdaptedRelationshipDescriptors.toModelType().getValue());
    }

    @Test
    public void toModelType_nullRolelessDescriptors_throwsIllegalValueException() {
        ArrayList<ArrayList<String>> roleBasedDescriptors = new ArrayList<>();
        roleBasedDescriptors.add(new ArrayList<>(Arrays.asList("bioparents", "parent", "child")));
        roleBasedDescriptors.add(new ArrayList<>(Arrays.asList("spouses", "husband", "wife")));

        JsonAdaptedRelationshipDescriptors jsonAdaptedRelationshipDescriptors =
                new JsonAdaptedRelationshipDescriptors(null, roleBasedDescriptors);
        assertThrows(IllegalValueException.class, jsonAdaptedRelationshipDescriptors::toModelType);
    }

    @Test
    public void toModelType_nullRoleBasedDescriptors_throwsIllegalValueException() {
        ArrayList<String> rolelessDescriptors = new ArrayList<>(Arrays.asList("friend", "colleagues"));

        JsonAdaptedRelationshipDescriptors jsonAdaptedRelationshipDescriptors =
                new JsonAdaptedRelationshipDescriptors(rolelessDescriptors, null);
        assertThrows(IllegalValueException.class, jsonAdaptedRelationshipDescriptors::toModelType);
    }
}
