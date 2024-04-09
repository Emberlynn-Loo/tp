package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import seedu.address.commons.util.ResultContainer;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RelationshipUtil;
import seedu.address.model.person.relationship.RoleBasedRelationship;


/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final RelationshipUtil relationships;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        relationships = new RelationshipUtil();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the relationship list with {@code relationships}.
     * {@code relationships} must not contain duplicate relationships.
     */
    public void setRelationships(List<Relationship> relationships) {
        this.relationships.setRelationships(relationships);
    }

    public void setRelationshipDescriptors(Pair<ArrayList<String>,
            ArrayList<ArrayList<String>>> relationshipDescriptors) {
        this.relationships.setRelationshipDescriptors(relationshipDescriptors.getKey(),
                relationshipDescriptors.getValue());
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setRelationships(newData.getRelationshipList());
        ArrayList<String> rolelessDescriptors = newData.getRelationshipDescriptors().getRolelessDescriptors();
        ArrayList<ArrayList<String>> roleBasedDescriptors =
                newData.getRelationshipDescriptors().getRoleBasedDescriptors();
        Pair<ArrayList<String>, ArrayList<ArrayList<String>>> relationshipDescriptors =
                new Pair<>(rolelessDescriptors, roleBasedDescriptors);
        setRelationshipDescriptors(relationshipDescriptors);
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("relationships", relationships)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Relationship> getRelationshipList() {
        return relationships.asUnmodifiableObservableList();
    }

    @Override
    public RelationshipUtil getRelationshipDescriptors() {
        return relationships;
    }

    public void addRelationship(Relationship toAdd) {
        relationships.addRelationship(toAdd);
    }
    public void deleteRelationship(Relationship toDelete) {
        relationships.deleteRelationship(toDelete);
    };
    public boolean hasRelationship(Relationship toFind) {
        return relationships.hasRelationship(toFind);
    };
    public boolean hasRelationshipWithDescriptor(Relationship toFind) {
        return relationships.hasRelationshipWithDescriptor(toFind);
    };
    public String getExistingRelationship(Relationship toGet) {
        return relationships.getExistingRelationship(toGet);
    }

    public void deleteRelationshipsOfPerson(UUID personUuid) {
        relationships.deleteRelationshipsOfPerson(personUuid);
    }

    public ResultContainer anySearch(UUID originUuid, UUID targetUuid) {
        return relationships.anySearchForTreeMap(originUuid, targetUuid);
    }

    public ResultContainer familySearch(UUID originUuid, UUID targetUuid) {
        return relationships.familySearchForTreeMap(originUuid, targetUuid);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons)
                && relationships.equals(otherAddressBook.relationships);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

    public void deleteAttribute(String uuid, String attributeName) {
        persons.deleteAttribute(uuid, attributeName);
    }

    public Person getPersonByUuid(UUID id) throws CommandException {
        if (id == null) {
            throw new CommandException("Person not found.");
        }
        requireNonNull(id);
        return persons.getPersonByUuid(id);
    }

    public UUID getFullUuid(String digits) {
        requireNonNull(digits);
        return persons.getFullUuid(digits);
    }

    /**
     * Checks if the specified person identified by their UUID string has a particular attribute.
     * This method requires both the UUID of the person and the name of the attribute to be non-null.
     * It delegates the actual search to the {@code persons} data structure's {@code hasAttribute} method.
     *
     * @param uuidString The UUID of the person as a String. Must not be null.
     * @param attributeName The name of the attribute to check for. Must not be null.
     * @return {@code true} if the person has the specified attribute, {@code false} otherwise.
     * @throws NullPointerException if either {@code uuidString} or {@code attributeName} is null.
     */
    public boolean hasAttribute(String uuidString, String attributeName) {
        requireNonNull(uuidString);
        requireNonNull(attributeName);
        return persons.hasAttribute(uuidString, attributeName);
    }

    public void deleteRelationType(String relationType) {
        relationships.deleteRelationType(relationType);
    }

    public boolean isRelationRoleBased(String descriptor) {
        return relationships.isRelationRoleBased(descriptor);
    }

    public List<String> getRoles(String descriptor) {
        return relationships.getRoles(descriptor);
    }

    public boolean hasRelationshipWithRoles(RoleBasedRelationship relationship, UUID uuid, UUID uuid2) {
        return relationships.hasRelationshipWithRoles(relationship, uuid, uuid2);
    }

    public boolean isRelationRoleless(String descriptor) {
        return relationships.isRelationRoleless(descriptor);
    }

    public RoleBasedRelationship getBioparentsCount(Model model, String originUuid, String targetUuid, String role1,
                                                    String role2) throws CommandException {
        return relationships.getBioparentsCount(model, originUuid, targetUuid, role1, role2);
    }

    public String relationTypeExistsWithOrWithoutS(String relationshipDescriptor) {
        return relationships.relationTypeExistsWithOrWithoutS(relationshipDescriptor);
    }
}
