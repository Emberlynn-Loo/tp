package seedu.address.storage;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.util.Pair;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.relationship.RelationshipUtil;

/**
 * Jackson-friendly version of {@link RelationshipUtil}.
 */
public class JsonAdaptedRelationshipDescriptors {

    public final ArrayList<String> rolelessDescriptors;
    public final ArrayList<ArrayList<String>> roleBasedDescriptors;

    /**
     * Constructs a {@code JsonAdaptedRelationshipDescriptors} with the given relationship descriptors.
     */
    @JsonCreator
    public JsonAdaptedRelationshipDescriptors(@JsonProperty("rolelessDescriptors")
                                                  ArrayList<String> rolelessDescriptors,
                                             @JsonProperty("roleBasedDescriptors")
                                                  ArrayList<ArrayList<String>> roleBasedDescriptors) {
        this.rolelessDescriptors = rolelessDescriptors;
        this.roleBasedDescriptors = roleBasedDescriptors;
    }

    /**
     * Converts a given {@code RelationshipUtil} into this class for Jackson use.
     */
    public JsonAdaptedRelationshipDescriptors(RelationshipUtil source) {
        this.rolelessDescriptors = source.getRolelessDescriptors();
        this.roleBasedDescriptors = source.getRoleBasedDescriptors();
    }

    /**
     * Converts this Jackson-friendly adapted relationship descriptors object into the model's {@code RelationshipUtil}
     * object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the relationship descriptors.
     */
    public Pair<ArrayList<String>, ArrayList<ArrayList<String>>> toModelType() throws IllegalValueException {
        if (rolelessDescriptors == null || roleBasedDescriptors == null) {
            throw new IllegalValueException("Invalid relationship descriptors in JSON.");
        }
        return new Pair<>(rolelessDescriptors, roleBasedDescriptors);
    }
}
