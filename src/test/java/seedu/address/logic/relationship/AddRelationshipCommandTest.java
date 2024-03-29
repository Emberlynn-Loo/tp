package seedu.address.logic.relationship;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersonsUuid.getTypicalAddressBook;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.SiblingRelationship;
import seedu.address.model.person.relationship.SpousesRelationship;
import seedu.address.testutil.TypicalPersonsUuid;

class AddRelationshipCommandTest {
    private Model model;
    private Model expectedModel;

    @BeforeEach
    void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    void testExecute_duplicateInputThrowsException() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0001";
        String relationshipDescriptor = "housemates";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        assertCommandFailure(addRelationshipCommand, model,
                "Relationships must be between 2 different people");
    }

    @Test
    void execute_validInput_success() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "housemates";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        String expectedMessage = "Add success";
        UUID person1Uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID person2Uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
        String familyRelationshipDescriptor = "housemates";
        expectedModel.addRelationship(
                new Relationship(person1Uuid, person2Uuid, familyRelationshipDescriptor));
        assertCommandSuccess(addRelationshipCommand, model, expectedMessage, expectedModel);
    }

    @Test
    void testExecute_invalidUuidInputThrowsException() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0019";
        String relationshipDescriptor = "housemates";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        assertCommandFailure(addRelationshipCommand, model,
                "The UUID provided is invalid: ");
    }

    @Test
    void testExecuteAddingExistingRelationshipThrowsException() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "housemates";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        UUID person1Uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID person2Uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
        String familyRelationshipDescriptor = "housemates";
        model.addRelationship(new Relationship(person1Uuid, person2Uuid, familyRelationshipDescriptor));
        assertCommandFailure(addRelationshipCommand, model,
                "Sorry, 00000000-0000-0000-0000-000000000001 and 00000000-0000-0000-0000-000000000002 are housemates");

    }
    @Test
    void testExecuteAddInvalidRelationshipDescriptorThrowsException() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "123";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        assertCommandFailure(addRelationshipCommand, model,
                "Invalid Relationship type. Must only consist of letters.");
    }
    @Test
    void testEqualsMethodWithSameArguments() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "housemates";
        AddRelationshipCommand test1 =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        AddRelationshipCommand test2 =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        Assertions.assertEquals(test1.equals(test2), true);
    }
    @Test
    void testEqualsMethodWithNotAddRelationshipCommandInstance() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "housemates";
        AddRelationshipCommand test1 =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        String test2 = "test";
        Assertions.assertEquals(test1.equals(test2), false);
    }
    @Test
    void testEqualsMethodWithSameInstance() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "housemates";
        AddRelationshipCommand test1 =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        Assertions.assertEquals(test1.equals(test1), true);
    }

    @Test
    void execute_addSiblingRelationship_success() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "Siblings";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        String expectedMessage = "Add success";
        UUID person1Uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID person2Uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
        expectedModel.addRelationship(
                new SiblingRelationship(person1Uuid, person2Uuid, "brother", "sister"));
        assertCommandSuccess(addRelationshipCommand, model, expectedMessage, expectedModel);
    }

    @Test
    void execute_addSpousesRelationship_success() {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "Spouses";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        String expectedMessage = "Add success";
        UUID person1Uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID person2Uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
        expectedModel.addRelationship(
                new SpousesRelationship(person1Uuid, person2Uuid, "husband", "husband"));
        assertCommandSuccess(addRelationshipCommand, model, expectedMessage, expectedModel);
    }

    @Test
    void execute_validInputs_success() throws CommandException {
        String testOriginUuid = "0001";
        String testTargetUuid = "0002";
        String relationshipDescriptor = "housemates";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(testOriginUuid, testTargetUuid, relationshipDescriptor);
        String expectedMessage = "Add success";
        UUID person1Uuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID person2Uuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
        String familyRelationshipDescriptor = "housemates";

        // Ensure model does not initially contain the relationship
        Assertions.assertEquals(model.hasRelationship(new Relationship(person1Uuid, person2Uuid,
                familyRelationshipDescriptor)), false);

        // Execute the command
        CommandResult commandResult = addRelationshipCommand.execute(model);

        // Ensure model now contains the relationship
        Assertions.assertEquals(model.hasRelationship(new Relationship(person1Uuid, person2Uuid,
                familyRelationshipDescriptor)), true);

        // Ensure command result contains the expected message
        Assertions.assertEquals(commandResult.getFeedbackToUser(), expectedMessage);
    }

    @Test
    public void execute_validInputWithNewRelationshipDescriptorSiblings_success() {
        // Setup
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String newRelationshipDescriptor = "siblings";
        String role1 = "brother";
        String role2 = "sister";
        AddRelationshipCommand editCommand = new AddRelationshipCommand(originUuid,
                targetUuid, newRelationshipDescriptor, role1, role2);

        // Execute
        CommandResult result = Assertions.assertDoesNotThrow(() -> editCommand.execute(model));

        // Verify
        Assertions.assertEquals(AddRelationshipCommand.MESSAGE_ADD_RELATIONSHIP_SUCCESS, result.getFeedbackToUser());

        // Assert that the relationship was deleted and added successfully
        UUID fullOriginUuid = model.getFullUuid(originUuid);
        UUID fullTargetUuid = model.getFullUuid(targetUuid);
        Relationship expectedAddedRelationship =
                new SiblingRelationship(fullOriginUuid, fullTargetUuid, role1, role2);

        Assertions.assertTrue(model.hasRelationshipWithDescriptor(expectedAddedRelationship));
    }

    @Test
    public void execute_validInputWithNewRelationshipDescriptorSpouses_success() {
        // Setup
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String newRelationshipDescriptor = "spouses";
        String role1 = "husband";
        String role2 = "husband";
        AddRelationshipCommand editCommand = new AddRelationshipCommand(originUuid,
                targetUuid, newRelationshipDescriptor, role1, role2);

        // Execute
        CommandResult result = Assertions.assertDoesNotThrow(() -> editCommand.execute(model));

        // Verify
        Assertions.assertEquals(AddRelationshipCommand.MESSAGE_ADD_RELATIONSHIP_SUCCESS, result.getFeedbackToUser());

        // Assert that the relationship was deleted and added successfully
        UUID fullOriginUuid = model.getFullUuid(originUuid);
        UUID fullTargetUuid = model.getFullUuid(targetUuid);
        Relationship expectedAddedRelationship =
                new SpousesRelationship(fullOriginUuid, fullTargetUuid, role1, role2);

        Assertions.assertTrue(model.hasRelationshipWithDescriptor(expectedAddedRelationship));
    }

    @Test
    public void execute_validInputWithNewRelationshipDescriptorNotSpouses_nothingAddedOrDeleted() {
        // Setup
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String newRelationshipDescriptor = "workbuddies";
        String role1 = "subordinate";
        String role2 = "boss";
        AddRelationshipCommand editCommand = new AddRelationshipCommand(originUuid,
                targetUuid, newRelationshipDescriptor, role1, role2);

        // Execute
        CommandResult result = Assertions.assertDoesNotThrow(() -> editCommand.execute(model));

        // Verify
        Assertions.assertEquals(AddRelationshipCommand.MESSAGE_ADD_RELATIONSHIP_SUCCESS, result.getFeedbackToUser());

        // Assert that no relationship was added or deleted
        Assertions.assertTrue(model.hasRelationshipWithDescriptor(
                new Relationship(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        UUID.fromString("00000000-0000-0000-0000-000000000002"), newRelationshipDescriptor)));
    }
}
