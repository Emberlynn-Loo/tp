package seedu.address.logic.relationship;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Lists all the relationship types in the address book.
 */
public class ListRelationshipTypesCommand extends Command {
    public static final String COMMAND_WORD = "listrelations";

    /**
     * Executes the command to list all the relationship types in the address book.
     * @param model {@code Model} which the command should operate on.
     * @return a CommandResult containing the success message to be displayed to the user.
     * @throws CommandException if an error occurs during command execution.
     */
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        String successMessage = model.showRelationshipTypes();
        return new CommandResult(successMessage);
    }
}
