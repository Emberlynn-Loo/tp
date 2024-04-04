package seedu.address.logic.relationship;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.model.person.relationship.RoleBasedRelationship;
import seedu.address.testutil.TypicalPersonsUuid;

public class EditRelationshipCommandTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");

    private Model model;
    private Model expectedModel;

    @Test
    public void execute_originUuidIsNull_throwsCommandException() {
        // Setup
        Model model = new ModelManager();
        String targetUuid = "target123";
        String oldRelationshipDescriptor = "siblings";
        String newRelationshipDescriptor = "friend";
        EditRelationshipCommand editCommand = new EditRelationshipCommand("000000", targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor);

        // Verify
        assertThrows(CommandException.class, () -> editCommand.execute(model),
                Messages.MESSAGE_INVALID_PERSON_UUID);
    }

    @Test
    public void execute_invalidTargetUuid_throwsCommandException() {
        Model model = new ModelManager();
        String originUuid = "1234";
        String oldDescriptor = "siblings";
        String newDescriptor = "friend";
        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, "00000",
                oldDescriptor, newDescriptor);
        assertThrows(CommandException.class, () -> editCommand.execute(model),
                Messages.MESSAGE_INVALID_PERSON_UUID);
    }

    @Test
    public void execute_relationshipToEditDoesNotExist_throwsCommandException() {
        // Setup
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0003";
        String oldDescriptor = "siblings";
        String newDescriptor = "friend";

        // Attempt to edit a non-existent relationship
        EditRelationshipCommand editCommand = new EditRelationshipCommand(
                originUuid, targetUuid, oldDescriptor, newDescriptor);
        Relationship toEditOff = new Relationship(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000003"), oldDescriptor);
        // Verify
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                String.format("Sorry %s do not exist", toEditOff));

        // Check the exception message
        assertEquals(String.format("Sorry %s do not exist", toEditOff),
                exception.getMessage());

    }

    @Test
    public void execute_existingRelationships_throwsCommandException() {
        // Setup
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldDescriptor = "friend";
        String newDescriptor = "siblings";

        // Add the relationship to the model
        Relationship oldRelationship = new Relationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldDescriptor);
        Relationship existingRelationship = new Relationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), newDescriptor);
        model.addRelationship(oldRelationship);
        model.addRelationship(existingRelationship);

        // Attempt to edit the relationship with the same descriptor
        EditRelationshipCommand editCommand = new EditRelationshipCommand(
                originUuid, targetUuid, oldDescriptor, newDescriptor);

        // Verify
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                String.format("%s already exists", existingRelationship));

        // Check the exception message
        assertEquals(String.format("Sorry, %s", existingRelationship),
                exception.getMessage());
    }

    @Test
    public void execute_invalidRelationshipType_throwsCommandException() {
        // Setup
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0006";
        String oldRelationshipDescriptor = "bioparents";
        String newRelationshipDescriptor = "1234";
        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor);

        // Verify
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                "Invalid Relationship type");
        // Check the exception message
        assertEquals("Invalid Relationship type. Must only consist of letters.",
                exception.getMessage());
    }

    @Test
    public void execute_roleBasedRelationshipValidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "siblings";
        String newRelationshipDescriptor = "bioparents";
        String role1 = "parent";
        String role2 = "child";

        Relationship oldRelationship = new Relationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor);
        model.addRelationship(oldRelationship);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role1, role2);
        assertDoesNotThrow(() -> editCommand.execute(model));
    }

    @Test
    public void execute_roleBasedRelationshipSibingsValidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "something";
        String newRelationshipDescriptor = "siblings";
        String role1 = "brother";
        String role2 = "sister";

        Relationship oldRelationship = new Relationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor);
        model.addRelationship(oldRelationship);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role1, role2);
        assertDoesNotThrow(() -> editCommand.execute(model));
    }

    @Test
    public void execute_roleBasedRelationshipSpousesValidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String newRelationshipDescriptor = "spouses";
        String role1 = "husband";
        String role2 = "wife";

        Relationship oldRelationship = new Relationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor);
        model.addRelationship(oldRelationship);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role1, role2);
        assertDoesNotThrow(() -> editCommand.execute(model));
    }

    @Test
    public void execute_roleBasedRelationshipFriendsValidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String newRelationshipDescriptor = "friends";
        String role1 = "smt";
        String role2 = "smtt";

        Relationship oldRelationship = new Relationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor);
        model.addRelationship(oldRelationship);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role1, role2);
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                "Friends cannot have roles");

        // Check the exception message
        assertEquals("Sorry, friends cannot have roles", exception.getMessage());
    }

    @Test
    public void execute_roleBasedRelationshipSameInValidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String newRelationshipDescriptor = "smt";
        String role1 = "role";
        String role2 = "rolee";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role1, role2);
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                "Expected CommandException");
        assertEquals("There's no need to edit the relationship if the new relationship is the same as the old one.",
                exception.getMessage());
    }

    @Test
    public void execute_roleBasedRelationshipSameValidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String newRelationshipDescriptor = "smt";
        String role1 = "role";
        String role2 = "rolee";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role2, role1);
        assertDoesNotThrow(() -> editCommand.execute(model));
    }

    @Test
    public void execute_hasDescriptorInvalidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String newRelationshipDescriptor = "smtelse";
        String role1 = "role";
        String role2 = "rolee";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolebasedDescriptor(newRelationshipDescriptor, role1, role2);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, "role", "rolee");

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor);
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                "Expected CommandException");
        assertEquals("Sorry, you added smtelse as a role based relationship."
                        + "\nIf you want to use it, please use the roles you added: [role, rolee]"
                        + "\nIf you want to make it a role based relationship, please delete the"
                        + "\nrelationtype and add it again.",
                exception.getMessage());
    }

    @Test
    public void execute_hasDescriptorFalseInvalidsRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smttt";
        String newRelationshipDescriptor = "smtelsee";
        String role1 = "role";
        String role2 = "rolee";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolelessDescriptor(newRelationshipDescriptor);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role1, role2);
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                "Expected CommandException");
        assertEquals("Sorry, you did not add smtelsee as a "
                        + "role based relationship."
                        + "\nIf you want to use it, please delete the roles"
                        + "\nIf you want to make it a role based relationship, please delete the "
                        + "relationtype and add it again.",
                exception.getMessage());
    }

    @Test
    public void execute_hasDescriptorFalseInvalidRoles_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String newRelationshipDescriptor = "smtelse";
        String role1 = "role";
        String role2 = "rolee";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolelessDescriptor(newRelationshipDescriptor);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role1, role2);
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                "Expected CommandException");
        assertEquals("Sorry, you did not add smtelse as a "
                        + "role based relationship."
                        + "\nIf you want to use it, please delete the roles"
                        + "\nIf you want to make it a role based relationship, please delete the "
                        + "relationtype and add it again.",
                exception.getMessage());
    }

    @Test
    public void execute_isRelationRoleBasedFalseInvalidRoles_doesNotThrowException() {
        // Setup
        Model model = new ModelManager();
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String newRelationshipDescriptor = "bioparents";
        String role1 = "parent";
        String role2 = "child";

        // Add old relationship to the model
        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolebasedDescriptor(newRelationshipDescriptor, role1, role2);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        // Execute command
        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, "UUU", role2);
    }

    @Test
    public void execute_isRelationRoleBasedFalseInvalidRole_doesNotThrowException() {
        // Setup
        Model model = new ModelManager();
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String newRelationshipDescriptor = "smtelse";
        String role1 = "role";
        String role2 = "rolee";

        // Add old relationship to the model
        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor,
                role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolebasedDescriptor(newRelationshipDescriptor, role1, role2);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        // Execute command
        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, newRelationshipDescriptor, role1, "dd");
    }

    @Test
    public void execute_hasDescriptorInvalidRoleless_addsRelationship() {
        Model model = new ModelManager();
        AddressBook typicalPersonsAddressBook = TypicalPersonsUuid.getTypicalAddressBook();
        model.setAddressBook(typicalPersonsAddressBook);
        String originUuid = "0001";
        String targetUuid = "0002";
        String oldRelationshipDescriptor = "smt";
        String role1 = "role";
        String role2 = "rolee";

        Relationship oldRelationship = new RoleBasedRelationship(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"), oldRelationshipDescriptor, role1, role2);
        model.addRelationship(oldRelationship);
        model.addRolebasedDescriptor(oldRelationshipDescriptor, role1, role2);

        EditRelationshipCommand editCommand = new EditRelationshipCommand(originUuid, targetUuid,
                oldRelationshipDescriptor, oldRelationshipDescriptor, role1, role2);
        CommandException exception = assertThrows(CommandException.class, () -> editCommand.execute(model),
                "Expected CommandException");
        assertEquals("There's no need to edit the relationship "
                        + "if the new relationship is the same as the old one.",
                exception.getMessage());
    }
}
