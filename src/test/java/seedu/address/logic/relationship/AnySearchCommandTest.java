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

public class AnySearchCommandTest {
    private Model model = new ModelManager(TypicalPersonsUuid.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validUuids_success() throws CommandException {
        AnySearchCommand anySearchCommand = new AnySearchCommand("0001", "0002");
        CommandResult commandResult = anySearchCommand.execute(model);
        String expectedSearchResult = "Pathway:\n"
                + "0001 --> friend of --> 0002";
        System.out.println(commandResult.getFeedbackToUser());
        assertEquals(expectedSearchResult, commandResult.getFeedbackToUser());
    }

    @Test
    public void execute_sameUuids_throwsCommandException() {
        AnySearchCommand anySearchCommand = new AnySearchCommand("0001", "0001");
        assertCommandFailure(anySearchCommand,
                model, "anySearch must be performed between two different persons.");
    }

    @Test
    public void execute_invalidUuids_throwsCommandException() {
        AnySearchCommand anySearchCommand1 = new AnySearchCommand(null, "0009");
        AnySearchCommand anySearchCommand2 = new AnySearchCommand("0009", null);
        assertCommandFailure(anySearchCommand1, model, Messages.MESSAGE_INVALID_PERSON_UUID);
        assertCommandFailure(anySearchCommand2, model, Messages.MESSAGE_INVALID_PERSON_UUID);
    }
    @Test
    public void testListFilteredCorrectlyForInvalidAnySearch() throws CommandException {
        AnySearchCommand anySearchCommand1 = new AnySearchCommand("0001", "000a");
        CommandResult result = anySearchCommand1.execute(model);
        assertEquals(0, (model.getFilteredPersonList().size()));
        assertEquals(0, model.getFilteredRelationshipList().size());
        assertEquals("No Relationship pathway found", result.getFeedbackToUser());
    }
    @Test
    public void testInputsNotInAddressBook() {
        AnySearchCommand anySearchCommand1 = new AnySearchCommand("0020", "0020");
        assertCommandFailure(anySearchCommand1, model, "you have not added the contacts "
                + "of the people you are looking for!");
    }
    @Test
    void testEqualsSameObject() {
        AnySearchCommand command = new AnySearchCommand("uuid1", "uuid2");
        assertTrue(command.equals(command));
    }

    @Test
    void testEqualsEquivalentObjects() {
        AnySearchCommand command1 = new AnySearchCommand("uuid1", "uuid2");
        AnySearchCommand command2 = new AnySearchCommand("uuid1", "uuid2");
        assertTrue(command1.equals(command2));
    }

    @Test
    void testEqualsDifferentOriginUuid() {
        AnySearchCommand command1 = new AnySearchCommand("uuid1", "uuid2");
        AnySearchCommand command2 = new AnySearchCommand("uuid3", "uuid2");
        assertFalse(command1.equals(command2));
    }

    @Test
    void testEqualsDifferentTargetUuid() {
        AnySearchCommand command1 = new AnySearchCommand("uuid1", "uuid2");
        AnySearchCommand command2 = new AnySearchCommand("uuid1", "uuid3");
        assertFalse(command1.equals(command2));
    }

    @Test
    void testEqualsBothUuidsDifferent() {
        AnySearchCommand command1 = new AnySearchCommand("uuid1", "uuid2");
        AnySearchCommand command2 = new AnySearchCommand("uuid3", "uuid4");
        assertFalse(command1.equals(command2));
    }
}
