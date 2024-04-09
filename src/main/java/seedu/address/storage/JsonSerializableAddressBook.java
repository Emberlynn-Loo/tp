package seedu.address.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javafx.util.Pair;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<JsonAdaptedPersonAttr> persons = new ArrayList<>();
    private final List<JsonAdaptedRelationship> relationships = new ArrayList<>();
    private final JsonAdaptedRelationshipDescriptors relationshipDescriptors;

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPersonAttr> persons,
                                       @JsonProperty("relationships") List<JsonAdaptedRelationship> relationships,
                                       @JsonProperty("relationshipDescriptors")
                                           JsonAdaptedRelationshipDescriptors relationshipDescriptors) {
        this.persons.addAll(persons);
        if (relationships != null) {
            this.relationships.addAll(relationships);
        }
        this.relationshipDescriptors = relationshipDescriptors;
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPersonAttr::new).collect(Collectors.toList()));
        relationships.addAll(
                source.getRelationshipList().stream().map(JsonAdaptedRelationship::new).collect(Collectors.toList()));
        relationshipDescriptors = new JsonAdaptedRelationshipDescriptors(source.getRelationshipDescriptors());
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPersonAttr jsonAdaptedPersonAttr : persons) {
            Person person = jsonAdaptedPersonAttr.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }
        Pair<ArrayList<String>, ArrayList<ArrayList<String>>> pairOfDescriptors = relationshipDescriptors.toModelType();

        for (JsonAdaptedRelationship jsonAdaptedRelationship : relationships) {
            Relationship relationship = jsonAdaptedRelationship.toModelType();
            if (!(relationship instanceof RoleBasedRelationship)
                    && !pairOfDescriptors.getKey().contains(relationship.getRelationshipDescriptor())) {
                throw new IllegalValueException("Invalid relationship descriptor "
                        + relationship.getRelationshipDescriptor() + " found");
            }
            if (relationship instanceof RoleBasedRelationship) {
                ArrayList<String> roleBasedDescriptor = new ArrayList<>(Arrays.asList(
                        relationship.getRelationshipDescriptor(), (
                                (RoleBasedRelationship) relationship).getRole(relationship.getPerson1()), (
                                        (RoleBasedRelationship) relationship).getRole(relationship.getPerson2())
                ));
                boolean descriptorExists = pairOfDescriptors.getValue().stream()
                        .anyMatch(descriptor -> isSameDescriptor(descriptor, roleBasedDescriptor));
                if (!descriptorExists) {
                    throw new IllegalValueException("Invalid role-based relationship descriptor "
                            + relationship.getRelationshipDescriptor() + " found");
                }
            }
            if (addressBook.hasRelationship(relationship)) {
                throw new IllegalValueException("Duplicate relationship found.");
            }
            addressBook.addRelationship(relationship);
        }
        if (pairOfDescriptors != null) {
            addressBook.setRelationshipDescriptors(pairOfDescriptors);
        }
        return addressBook;
    }

    private boolean isSameDescriptor(ArrayList<String> list1, ArrayList<String> list2) {
        ArrayList<String> sortedList1 = new ArrayList<>(list1);
        ArrayList<String> sortedList2 = new ArrayList<>(list2);
        Collections.sort(sortedList1);
        Collections.sort(sortedList2);
        return sortedList1.equals(sortedList2);
    }
}
