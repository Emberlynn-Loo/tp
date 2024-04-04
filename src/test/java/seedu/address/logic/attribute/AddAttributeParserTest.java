package seedu.address.logic.attribute;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddAttributeCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.AddAttributeParser;
import seedu.address.logic.parser.exceptions.ParseException;

class AddAttributeParserTest {

    private final AddAttributeParser parser = new AddAttributeParser();

    @Test
    void parse_validAddCommand_returnsAddAttributeCommand() throws ParseException {
        String userInput = "addAttribute /12345 /nickname Johnny";
        Command command = parser.parse(userInput);
        assertTrue(command instanceof AddAttributeCommand);
    }

    @Test
    void parse_incompleteCommand_throwsParseException() {
        String incompleteCommand = "addAttribute 12345";
        assertThrows(ParseException.class, () -> parser.parse(incompleteCommand));
    }

    @Test
    void parse_unknownCommand_throwsParseException() {
        String unknownCommand = "modifyAttribute 12345 nickname Johnny";
        assertThrows(ParseException.class, () -> parser.parse(unknownCommand));
    }

    @Test
    void parse_missingUuid_throwsParseException() {
        String userInput = "addAttribute /12 345 /nickname Johnny";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    void parse_invalidAttributeFormat_throwsParseException() {
        String userInput = "addAttribute /12345 /nickname";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
