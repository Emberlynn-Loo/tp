package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.util.ResultContainer;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;

public class AddCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        AddCommand addCommand = new AddCommand(new HashMap<>());
        assertThrows(NullPointerException.class, () -> addCommand.execute(null));
    }
    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        HashMap<String, String> aliceHashMap = new HashMap<>();
        aliceHashMap.put("Name", "Alice");
        AddCommand addCommand = new AddCommand(aliceHashMap);

        CommandResult commandResult = addCommand.execute(modelStub);
        System.out.println(commandResult.getFeedbackToUser());
        assertEquals("New person added.\nDetails:\nName: Alice", commandResult.getFeedbackToUser());
    }

    @Test
    public void equals() {
        HashMap<String, String> aliceHashMap = new HashMap<>();
        HashMap<String, String> bobHashMap = new HashMap<String, String>();
        aliceHashMap.put("Name", "Alice");
        bobHashMap.put("Name", "Bob");
        AddCommand addAliceCommand = new AddCommand(aliceHashMap);
        AddCommand addBobCommand = new AddCommand(bobHashMap);

        // same object -> returns true
        assertEquals(addAliceCommand, addAliceCommand);

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(aliceHashMap);
        assertEquals(addAliceCommand, addAliceCommandCopy);

        // different types -> returns false
        assertNotEquals(1, addAliceCommand);

        // null -> returns false
        assertNotEquals(null, addAliceCommand);

        // different person -> returns false
        assertNotEquals(addAliceCommand, addBobCommand);
    }

    @Test
    public void toStringMethod() {
        HashMap<String, String> aliceHashMap = new HashMap<>();
        aliceHashMap.put("Name", "Alice");
        AddCommand addCommand = new AddCommand(aliceHashMap);
        String expected = AddCommand.class.getCanonicalName() + "{attributeMap={Name=Alice}}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    public class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public void updateFilteredRelationshipList(Predicate<Relationship> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteAttribute(String uuid, String attributeName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasRelationship(Relationship target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasRelationshipWithDescriptor(Relationship target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addRelationship(Relationship target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteRelationship(Relationship toDelete) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String getExistingRelationship(Relationship toGet) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteRelationshipsOfPerson(UUID personUuid) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<String> getRoles(String descriptor) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ResultContainer anySearch(UUID originUuid, UUID targetUuid) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ResultContainer familySearch(UUID originUuid, UUID targetUuid) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addRolelessDescriptor(String newRelationshipDescriptor) {

        }

        @Override
        public void addRolebasedDescriptor(String newRelationshipDescriptor, String role1, String role2) {
        }

        @Override
        public void resetRelationshipDescriptors() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasRelationshipWithRoles(RoleBasedRelationship relationship, UUID uuid, UUID uuid2) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isRelationRoleless(String descriptor) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public RoleBasedRelationship getBioparentsCount(Model model, String originUuid, String targetUuid,
                                                        String role1, String role2) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public RoleBasedRelationship checkSiblingsSpousesGender(Model model, String originUuid, String targetUuid,
                                                                String rolePerson1, String rolePerson2,
                                                                Boolean isSiblings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void genderCheck(UUID uuid, String gender) throws CommandException {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void genderMatch(String rolePerson1, String uuid, String shortUuid) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Relationship> getFilteredRelationshipList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Person getPersonByUuid(UUID id) {
            throw new AssertionError("this method should not be called");
        }

        public boolean hasAttribute(String uuidString, String attributeName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public String showRelationshipTypes() {
            return null;
        }

        @Override
        public void deleteRelationType(String relationType) {
        }

        @Override
        public boolean isRelationRoleBased(String descriptor) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public UUID getFullUuid(String digits) {
            throw new AssertionError("This method should not be called");
        }

    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.equals(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::equals);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
