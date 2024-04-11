package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AnySearchCommand;

public class AnySearchCommandParserTest {
    private AnySearchCommandParser parser = new AnySearchCommandParser();

    @Test
    public void parse_validArgs_returnsAnySearchCommand() {
        String userInput = "/0001 /0002";
        AnySearchCommand expectedCommand = new AnySearchCommand("0001", "0002");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "0001", "Invalid command format!");
        assertParseFailure(parser, "", "Invalid command format!");
    }
}
