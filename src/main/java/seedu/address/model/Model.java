package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.util.ResultContainer;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;
    /** {@code Predicate} that always evaluate to false */
    Predicate<Person> PREDICATE_SHOW_NO_PERSONS = unused -> false;
    /** {@code Predicate} that always evaluate to true */
    Predicate<Relationship> PREDICATE_SHOW_ALL_RELATIONSHIPS = unused -> true;
    /** {@code Predicate} that always evaluate to false */
    Predicate<Relationship> PREDICATE_SHOW_NO_RELATIONSHIPS = unused -> false;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();
    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);
    void updateFilteredRelationshipList(Predicate<Relationship> predicate);
    boolean hasRelationship(Relationship target);

    void addRelationship(Relationship toAdd);

    void deleteRelationship(Relationship toDelete);

    String getExistingRelationship(Relationship toGet);

    boolean hasRelationshipWithDescriptor(Relationship target);

    /**
     * Returns an unmodifiable view of the filtered relationship list
     */
    ObservableList<Relationship> getFilteredRelationshipList();

    void deleteAttribute(String uuid, String attributeName);

    UUID getFullUuid(String digits);

    Person getPersonByUuid(UUID id) throws CommandException;

    boolean hasAttribute(String uuidString, String attributeName);

    String showRelationshipTypes();

    void deleteRelationType(String relationType);

    boolean isRelationRoleBased(String descriptor);

    void deleteRelationshipsOfPerson(UUID personUuid);

    List<String> getRoles(String descriptor);

    ResultContainer anySearch(UUID originUuid, UUID targetUuid);

    ResultContainer familySearch(UUID originUuid, UUID targetUuid);

    void addRolelessDescriptor(String newRelationshipDescriptor);
    void addRolebasedDescriptor(String newRelationshipDescriptor, String role1, String role2);
    void resetRelationshipDescriptors();

    boolean hasRelationshipWithRoles(RoleBasedRelationship relationship, UUID uuid, UUID uuid2);
    boolean isRelationRoleless(String descriptor);

    RoleBasedRelationship getBioparentsCount(Model model, String originUuid,
                                             String targetUuid, String role1, String role2) throws CommandException;
    String relationTypeExistsWithOrWithoutS(String relationshipDescriptor);
}
