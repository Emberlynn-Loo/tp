package seedu.address.storage;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddRelationshipCommand;
import seedu.address.model.person.relationship.BioParentsRelationship;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;
import seedu.address.model.person.relationship.SiblingRelationship;
import seedu.address.model.person.relationship.SpousesRelationship;

/**
 * Jackson-friendly version of {@link Relationship}.
 */
public class JsonAdaptedRelationship {
    public final String person1;
    public final String person2;
    private final String relationshipDescriptor;
    private final String rolePerson1;
    private final String rolePerson2;

    /**
     * Constructs a {@code JsonAdaptedRelationship} with the given relationship details.
     */
    @JsonCreator
    public JsonAdaptedRelationship(@JsonProperty("person1") String person1,
                                   @JsonProperty("person2") String person2,
                                   @JsonProperty("relationshipDescriptor") String relationshipDescriptor,
                                   @JsonProperty("rolePerson1") String rolePerson1,
                                   @JsonProperty("rolePerson2") String rolePerson2) {
        this.person1 = person1;
        this.person2 = person2;
        this.relationshipDescriptor = relationshipDescriptor;
        this.rolePerson1 = rolePerson1;
        this.rolePerson2 = rolePerson2;
    }

    /**
     * Converts a given {@code Relationship} into this class for Jackson use.
     */
    public JsonAdaptedRelationship(Relationship source) {
        this.person1 = source.getPerson1().toString();
        this.person2 = source.getPerson2().toString();
        this.relationshipDescriptor = source.getRelationshipDescriptor();
        if (source instanceof RoleBasedRelationship) {
            RoleBasedRelationship roleBasedSource = (RoleBasedRelationship) source;
            rolePerson1 = roleBasedSource.getRoleDescriptor(source.getPerson1());
            rolePerson2 = roleBasedSource.getRoleDescriptor(source.getPerson2());
        } else {
            rolePerson1 = null;
            rolePerson2 = null;
        }
    }

    /**
     * Converts this Jackson-friendly adapted relationship object into the model's {@code Relationship} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted relationship.
     */
    public Relationship toModelType() throws IllegalValueException {
        if (relationshipDescriptor == null || relationshipDescriptor.isEmpty() || person1 == null || person2 == null) {
            throw new IllegalValueException("Invalid relationship type or value in JSON.");
        }
        String descriptorCheck = relationshipDescriptor.toLowerCase();
        if (descriptorCheck.contains("family")) {
            throw new IllegalValueException("Relation type family is not allowed in JSON.");
        }
        if (rolePerson1 != null && rolePerson2 != null) {
            switch (descriptorCheck) {
            case "bioparents":
                if ((!rolePerson1.equals("parent") && !rolePerson2.equals("parent"))
                        || ((!rolePerson1.equals("child") && !rolePerson2.equals("child")))) {
                    throw new IllegalValueException("Bioparents relationship must have one parent and child role.");
                }
                return new BioParentsRelationship(UUID.fromString(person1), UUID.fromString(person2), rolePerson1,
                        rolePerson2);
            case "siblings":
                if (!((rolePerson1.equals("brother") && rolePerson2.equals("brother"))
                        || (rolePerson1.equals("sister") && rolePerson2.equals("sister"))
                        || (rolePerson1.equals("brother") && rolePerson2.equals("sister"))
                        || (rolePerson1.equals("sister") && rolePerson2.equals("brother")))) {
                    throw new IllegalValueException("Siblings relationship must have 'brother' or 'sister' role.");
                }
                return new SiblingRelationship(UUID.fromString(person1), UUID.fromString(person2), rolePerson1,
                        rolePerson2);
            case "spouses":
                if (!((rolePerson1.equals("husband") && rolePerson2.equals("husband"))
                        || (rolePerson1.equals("wife") && rolePerson2.equals("wife"))
                        || (rolePerson1.equals("husband") && rolePerson2.equals("wife"))
                        || (rolePerson1.equals("wife") && rolePerson2.equals("husband")))) {
                    throw new IllegalValueException("Spouses relationship must have 'husband' or 'wife' role.");
                }
                return new SpousesRelationship(UUID.fromString(person1), UUID.fromString(person2), rolePerson1,
                        rolePerson2);
            default:
                if (!rolePerson1.matches("[a-zA-Z]+")) {
                    throw new IllegalValueException(rolePerson1 + " contains more than 1 word or special characters");
                }
                if (!rolePerson2.matches("[a-zA-Z]+")) {
                    throw new IllegalValueException(rolePerson2 + " contains more than 1 word or special characters");
                }
                return new RoleBasedRelationship(UUID.fromString(person1), UUID.fromString(person2),
                        relationshipDescriptor, rolePerson1, rolePerson2);
            }
        } else {
            if (AddRelationshipCommand.containsIllegalDescriptors(descriptorCheck)
                    || descriptorCheck.equals("siblings") || descriptorCheck.equals("spouses")
                    || descriptorCheck.equals("bioparents")) {
                throw new IllegalValueException("Relation type " + relationshipDescriptor
                        + " is not allowed in a roleless relationship.");
            }
            return new Relationship(UUID.fromString(person1), UUID.fromString(person2), relationshipDescriptor);
        }
    }
}
