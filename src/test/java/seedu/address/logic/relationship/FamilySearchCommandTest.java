package seedu.address.logic.relationship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersonsUuid;

public class FamilySearchCommandTest {
    private Model model = new ModelManager(TypicalPersonsUuid.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validUuids_success() throws CommandException {
        FamilySearchCommand familySearchCommand = new FamilySearchCommand("0001", "0002");
        CommandResult commandResult = familySearchCommand.execute(model);
        String expectedSearchResult = "No Relationship pathway found";
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
}
