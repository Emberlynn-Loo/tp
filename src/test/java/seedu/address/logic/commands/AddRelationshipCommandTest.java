package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersonsUuid.getTypicalAddressBook;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;
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
                Messages.MESSAGE_INVALID_PERSON_UUID + "0001 and 0019");
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
        AddRelationshipCommand addCommand = new AddRelationshipCommand(originUuid,
                targetUuid, newRelationshipDescriptor, role1, role2);

        // Execute
        CommandResult result = Assertions.assertDoesNotThrow(() -> addCommand.execute(model));

        // Verify
        Assertions.assertEquals(AddRelationshipCommand.MESSAGE_ADD_RELATIONSHIP_SUCCESS, result.getFeedbackToUser());

        // Assert that no relationship was added or deleted
        Assertions.assertTrue(model.hasRelationshipWithDescriptor(
                new Relationship(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        UUID.fromString("00000000-0000-0000-0000-000000000002"), newRelationshipDescriptor)));
    }

    @Test
    public void execute_validInputWithNewRelationshipDescriptorFriends_nothingAddedOrDeleted() {
        // Setup
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String newRelationshipDescriptor = "friends";
        String role1 = "subordinate";
        String role2 = "boss";
        AddRelationshipCommand addCommand = new AddRelationshipCommand(originUuid,
                targetUuid, newRelationshipDescriptor, role1, role2);
        assertCommandFailure(addCommand, model,
                "Sorry, friends cannot have roles");
    }

    @Test
    public void execute_hasDescriptorFalseInvalidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smttttt";
        String rolelessDescriptor = "smtelseee";
        String role1 = "role";
        String role2 = "rolee";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolelessDescriptor(rolelessDescriptor);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        AddRelationshipCommand addCommand = new AddRelationshipCommand(originUuid, targetUuid,
                rolelessDescriptor, role1, role2);
        CommandException exception = assertThrows(CommandException.class, () -> addCommand.execute(model),
                "Expected CommandException");
        assertEquals("Sorry, you did not add smtelseee as a "
                        + "role based relationship."
                        + "\nIf you want to use it, please delete the roles"
                        + "\nIf you want to make it a role based relationship, please delete the "
                        + "relationtype and add it again.",
                exception.getMessage());
    }

    @Test
    public void execute_hasDescriptorFalseWrongRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smtttts";
        String rolebasedDescriptor = "smtelsees";
        String role1 = "role";
        String role2 = "rolee";
        String wrongRole1 = "wrong";
        String wrongRole2 = "wrongg";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolebasedDescriptor(rolebasedDescriptor, role1, role2);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        AddRelationshipCommand addCommand = new AddRelationshipCommand(originUuid, targetUuid,
                rolebasedDescriptor, wrongRole1, wrongRole2);
        CommandException exception = assertThrows(CommandException.class, () -> addCommand.execute(model),
                "Expected CommandException");
        assertEquals("Please use the roles you added: [role, rolee]"
                        + "\nIf you want to change the roles, please delete the"
                        + "\nrelationtype and add it again.",
                exception.getMessage());
    }

    @Test
    public void execute_hasDescriptorFalseWrongRolesFlip_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smttttth";
        String rolebasedDescriptor = "smtelseeep";
        String role1 = "role";
        String role2 = "rolee";
        String wrongRole1 = "wrong";
        String wrongRole2 = "wrongg";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolebasedDescriptor(rolebasedDescriptor, role2, role1);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        AddRelationshipCommand addCommand = new AddRelationshipCommand(originUuid, targetUuid,
                rolebasedDescriptor, wrongRole1, wrongRole2);
        CommandException exception = assertThrows(CommandException.class, () -> addCommand.execute(model),
                "Expected CommandException");
        assertEquals("Please use the roles you added: [rolee, role]"
                        + "\nIf you want to change the roles, please delete the"
                        + "\nrelationtype and add it again.",
                exception.getMessage());
    }

    @Test
    public void execute_hasDescriptorFalseInvalidRolebased_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smttttt";
        String rolelessDescriptor = "smtelseee";
        String role1 = "role";
        String role2 = "rolee";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolebasedDescriptor(rolelessDescriptor, role1, role2);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        AddRelationshipCommand addCommand = new AddRelationshipCommand(originUuid, targetUuid,
                rolelessDescriptor);
        CommandException exception = assertThrows(CommandException.class, () -> addCommand.execute(model),
                "Expected CommandException");
        assertEquals("Sorry, you added smtelseee as a role based relationship."
                        + "\nIf you want to use it, please use the roles you added: [role, rolee]"
                        + "\nIf you want to make it a role based relationship, please delete the"
                        + "\nrelationtype and add it again.",
                exception.getMessage());
    }

    @Test
    public void execute_addThirdBioParentToPerson_throwsCommandException() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0005";
        String targetUuid = "0006";
        String role1 = "child";
        String role2 = "parent";
        String relationshipDescriptor = "bioparents";
        Relationship parentRelation2 = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000005"), relationshipDescriptor,
                role2, role1);
        Relationship parentRelation3 = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000005"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), relationshipDescriptor,
                role1, role2);
        model.addRelationship(parentRelation2);
        model.addRelationship(parentRelation3);
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(originUuid, targetUuid, relationshipDescriptor, role1, role2);
        assertCommandFailure(addRelationshipCommand, model,
                "Sorry, 0005 already has 2 bioparent relationships");
    }

    @Test
    public void execute_addPersonAsThirdBioParent_throwsCommandException() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0006";
        String targetUuid = "0005";
        String role1 = "parent";
        String role2 = "child";
        String relationshipDescriptor = "bioparents";
        Relationship parentRelation2 = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000005"), relationshipDescriptor,
                role1, role2);
        Relationship parentRelation3 = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000005"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), relationshipDescriptor,
                role2, role1);
        model.addRelationship(parentRelation2);
        model.addRelationship(parentRelation3);
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(originUuid, targetUuid, relationshipDescriptor, role1, role2);
        assertCommandFailure(addRelationshipCommand, model,
                "Sorry, 0005 already has 2 bioparent relationships");
    }

    @Test
    public void execute_addSecondBioParentToPerson_success() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0005";
        String targetUuid = "0006";
        String role1 = "child";
        String role2 = "parent";
        String relationshipDescriptor = "bioparents";
        AddRelationshipCommand addRelationshipCommand =
                new AddRelationshipCommand(originUuid, targetUuid, relationshipDescriptor, role1, role2);
        CommandResult result = Assertions.assertDoesNotThrow(() -> addRelationshipCommand.execute(model));
        Assertions.assertEquals(AddRelationshipCommand.MESSAGE_ADD_RELATIONSHIP_SUCCESS, result.getFeedbackToUser());
    }
}
