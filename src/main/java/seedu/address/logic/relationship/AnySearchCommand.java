package seedu.address.logic.relationship;

import java.util.ArrayList;
import java.util.UUID;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * This class is responsible for parsing and executing commands to search for the relationship pathway between two
 * persons in the address book.
 */
public class AnySearchCommand extends Command {

    public static final String COMMAND_WORD = "anySearch";

    private String originUuid;
    private String targetUuid;

    /**
     * Constructor takes in the string arguments needed to be passed into the relationship constructor and performs
     * the search for the relationship pathway.
     * @param originUuid
     * @param targetUuid
     */
    public AnySearchCommand(String originUuid, String targetUuid) {
        this.originUuid = originUuid;
        this.targetUuid = targetUuid;
    }

    /**
     * Executes the command to search for the relationship pathway between two persons
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
            throw new CommandException("anySearch must be performed between two different persons.");
        }
        ArrayList<String> searchResult = model.anySearch(fullOriginUuid, fullTargetUuid);
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
                || (other instanceof AnySearchCommand // instanceof handles nulls
                && originUuid.equals(((AnySearchCommand) other).originUuid)
                && targetUuid.equals(((AnySearchCommand) other).targetUuid));
    }
}
