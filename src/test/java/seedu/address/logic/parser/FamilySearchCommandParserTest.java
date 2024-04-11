package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FamilySearchCommand;

public class FamilySearchCommandParserTest {
    private FamilySearchCommandParser parser = new FamilySearchCommandParser();

    @Test
    public void parse_validArgs_returnsFamilySearchCommand() {
        String userInput = "/0001 /0002";
        FamilySearchCommand expectedCommand = new FamilySearchCommand("0001", "0002");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "0001", "Invalid command format!");
        assertParseFailure(parser, "", "Invalid command format!");
    }
}
