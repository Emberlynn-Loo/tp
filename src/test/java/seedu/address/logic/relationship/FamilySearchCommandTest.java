package seedu.address.logic.relationship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersonsUuid;

public class FamilySearchCommandTest {
    private Model model = new ModelManager(TypicalPersonsUuid.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validUuids_success() throws CommandException {
        FamilySearchCommand familySearchCommand = new FamilySearchCommand("0003", "0005");
        CommandResult commandResult = familySearchCommand.execute(model);
        String expectedSearchResult = "Pathway:\n"
                + "0003 -->  (bioparents) parent of  --> 0005";
        System.out.println(commandResult.getFeedbackToUser());
        assertEquals(expectedSearchResult, commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_sameUuids_throwsCommandException() {
        FamilySearchCommand familySearchCommand = new FamilySearchCommand("0001", "0001");
        assertCommandFailure(familySearchCommand,
                model, "familySearch must be performed between two different persons.");
    }

    @Test
    public void execute_invalidUuids_throwsCommandException() {
        FamilySearchCommand familySearchCommand1 = new FamilySearchCommand(null, "0009");
        FamilySearchCommand familySearchCommand2 = new FamilySearchCommand("0009", null);
        assertCommandFailure(familySearchCommand1, model, Messages.MESSAGE_INVALID_PERSON_UUID);
        assertCommandFailure(familySearchCommand2, model, Messages.MESSAGE_INVALID_PERSON_UUID);
    }
    @Test
    public void testListFilteredCorrectlyForInvalidAnySearch() throws CommandException {
        FamilySearchCommand anySearchCommand1 = new FamilySearchCommand("0001", "000a");
        CommandResult result = anySearchCommand1.execute(model);
        assertEquals(0, (model.getFilteredPersonList().size()));
        assertEquals(0, model.getFilteredRelationshipList().size());
        assertEquals("No Relationship pathway found", result.getFeedbackToUser());
    }
    @Test
    public void testInputsNotInAddressBook() {
        FamilySearchCommand familySearchCommand1 = new FamilySearchCommand("0020", "0020");
        assertCommandFailure(familySearchCommand1, model, "you have not added the contacts "
                + "of the people you are looking for!");
    }
    @Test
    void testEqualsSameObject() {
        FamilySearchCommand command = new FamilySearchCommand("uuid1", "uuid2");
        assertTrue(command.equals(command));
    }

    @Test
    void testEqualsEquivalentObjects() {
        FamilySearchCommand command1 = new FamilySearchCommand("uuid1", "uuid2");
        FamilySearchCommand command2 = new FamilySearchCommand("uuid1", "uuid2");
        assertTrue(command1.equals(command2));
    }

    @Test
    void testEqualsDifferentOriginUuid() {
        FamilySearchCommand command1 = new FamilySearchCommand("uuid1", "uuid2");
        FamilySearchCommand command2 = new FamilySearchCommand("uuid3", "uuid2");
        assertFalse(command1.equals(command2));
    }

    @Test
    void testEqualsDifferentTargetUuid() {
        FamilySearchCommand command1 = new FamilySearchCommand("uuid1", "uuid2");
        FamilySearchCommand command2 = new FamilySearchCommand("uuid1", "uuid3");
        assertFalse(command1.equals(command2));
    }

    @Test
    void testEqualsBothUuidsDifferent() {
        FamilySearchCommand command1 = new FamilySearchCommand("uuid1", "uuid2");
        FamilySearchCommand command2 = new FamilySearchCommand("uuid3", "uuid4");
        assertFalse(command1.equals(command2));
    }
}
