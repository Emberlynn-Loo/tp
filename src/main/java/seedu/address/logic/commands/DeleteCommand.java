package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String COMMAND_WORD_SHORT = "d";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: UUID (must be from a person who exists, 4 characters)\n"
            + "Example: " + COMMAND_WORD + " /1bd4";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    private String target;

    public DeleteCommand(String target) {
        this.target = target;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        UUID targetUuid = model.getFullUuid(target);

        if (targetUuid == null) {
            if (target.isEmpty()) {
                throw new CommandException(Messages.MESSAGE_UUID_EMPTY + "\n" + MESSAGE_USAGE);
            }
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_UUID + target + "\n" + MESSAGE_USAGE);
        }

        Person personToDelete = model.getPersonByUuid(targetUuid);

        model.deletePerson(personToDelete);
        model.deleteRelationshipsOfPerson(targetUuid);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return target.equals(otherDeleteCommand.target);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("target", target.toString())
                .toString();
    }
}
