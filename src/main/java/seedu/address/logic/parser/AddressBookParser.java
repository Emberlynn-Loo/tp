package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddAttributeCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddRelationshipCommand;
import seedu.address.logic.commands.AnySearchCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteAttributeCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteRelationshipCommand;
import seedu.address.logic.commands.EditAttributeCommand;
import seedu.address.logic.commands.EditRelationshipCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FamilySearchCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListRelationshipTypesCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(AddressBookParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException, CommandException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord").toLowerCase();
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_WORD_SHORT:
            return new AddCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_WORD_SHORT:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_WORD_SHORT:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_WORD_SHORT:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_WORD_SHORT:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_WORD_SHORT:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
        case HelpCommand.COMMAND_WORD_SHORT:
            return new HelpCommand();

        case AddRelationshipCommand.COMMAND_WORD:
        case AddRelationshipCommand.COMMAND_WORD_SHORT:
            return new AddRelationshipCommandParser().parse(arguments.trim());

        case DeleteRelationshipCommand.COMMAND_WORD:
        case DeleteRelationshipCommand.COMMAND_WORD_SHORT:
            return new DeleteRelationshipCommandParser().parse(arguments.trim());

        case EditRelationshipCommand.COMMAND_WORD:
        case EditRelationshipCommand.COMMAND_WORD_SHORT:
            return new EditRelationshipCommandParser().parse(arguments.trim());

        case ListRelationshipTypesCommand.COMMAND_WORD:
        case ListRelationshipTypesCommand.COMMAND_WORD_SHORT:
            return new ListRelationshipTypesCommand();

        case AnySearchCommand.COMMAND_WORD:
        case AnySearchCommand.COMMAND_WORD_SHORT:
            return new AnySearchCommandParser().parse(arguments.trim());

        case FamilySearchCommand.COMMAND_WORD:
        case FamilySearchCommand.COMMAND_WORD_SHORT:
            return new FamilySearchCommandParser().parse(arguments.trim());

        case DeleteAttributeCommand.COMMAND_WORD:
        case DeleteAttributeCommand.COMMAND_WORD_SHORT:
            return new DeleteAttributeCommandParser().parse(arguments.trim());

        case AddAttributeCommand.COMMAND_WORD:
        case AddAttributeCommand.COMMAND_WORD_SHORT:
            return new AddAttributeParser().parse(userInput);

        case EditAttributeCommand.COMMAND_WORD:
        case EditAttributeCommand.COMMAND_WORD_SHORT:
            return new EditCommandParser().parse(userInput.trim());

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }
}
