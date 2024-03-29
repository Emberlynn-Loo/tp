package seedu.address.logic.relationship;

import java.util.ArrayList;
import java.util.UUID;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * This class is responsible for parsing and executing commands to search for the family relationship pathway
 * between two persons in the address book.
 */
public class FamilySearchCommand extends Command {

    public static final String COMMAND_WORD = "familySearch";

    private String originUuid;
    private String targetUuid;

    /**
     * Constructor takes in the string arguments needed to be passed into the relationship constructor and performs
     * the search for the family relationship pathway.
     * @param originUuid
     * @param targetUuid
     */
    public FamilySearchCommand(String originUuid, String targetUuid) {
        this.originUuid = originUuid;
        this.targetUuid = targetUuid;
    }

    /**
     * Executes the command to search for the family relationship pathway between two persons
     * @param model
     * @return CommandResult
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (originUuid == null || targetUuid == null) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_UUID);
        }
        UUID fullOriginUuid = model.getFullUuid(originUuid);
        UUID fullTargetUuid = model.getFullUuid(targetUuid);
        if (fullOriginUuid == fullTargetUuid) {
            throw new CommandException("familySearch must be performed between two different persons.");
        }
        ArrayList<String> searchResult = model.familySearch(fullOriginUuid, fullTargetUuid);
        String searchResultString = "Pathway between " + originUuid + " and "
                + targetUuid + ":\n";
        searchResultString += String.join("\n", searchResult);
        if (searchResult.isEmpty()) {
            searchResultString = Messages.MESSAGE_SEARCH_FAILURE;
        }
        return new CommandResult(searchResultString);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof seedu.address.logic.relationship.FamilySearchCommand // instanceof handles nulls
                && originUuid.equals(((seedu.address.logic.relationship.FamilySearchCommand) other).originUuid)
                && targetUuid.equals(((seedu.address.logic.relationship.FamilySearchCommand) other).targetUuid));
    }
}
