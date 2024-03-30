package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

public class EditAttributeCommandParserTest {
    private final EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_validArgs_returnsEditAttributeCommand() throws ParseException {

        String userInput = "editAttribute /1234 /Name John Doe /Phone 12345678";
        Command command = parser.parse(userInput);
        assertNotNull(command);
    }

    @Test
    public void parse_noUuid_throwsParseException() {
        String noUuid = "/nickname";
        assertThrows(ParseException.class, () -> parser.parse(noUuid));
    }
    @Test
    public void parse_noAttribute_throwsParseException() {
        String noAttribute = "12345";
        assertThrows(ParseException.class, () -> parser.parse(noAttribute));
    }
    @Test
    public void parse_malformedCommand_throwsParseException() {
        String malformedCommand = "12345 12345 /nickname";
        assertThrows(ParseException.class, () -> parser.parse(malformedCommand));
    }
    @Test
    public void parse_malformedCommand2_throwsParseException() {
        String malformedCommand = "12345 nickname";
        assertThrows(ParseException.class, () -> parser.parse(malformedCommand));
    }

    @Test
    public void parse_malformedCommand3_throwsParseException() {
        String malformedCommand = "12345 /nickname";
        assertThrows(ParseException.class, () -> parser.parse(malformedCommand));
    }

    @Test
    public void parse_missing_uuid() {
        String malformedCommand = "/nickname lol";
        assertThrows(ParseException.class, () -> parser.parse(malformedCommand));
    }

}
