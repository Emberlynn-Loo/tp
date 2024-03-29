package seedu.address.logic.relationship;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

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
        assertParseFailure(parser, "0001", "Invalid command format! \n%1$s");
        assertParseFailure(parser, "", "Invalid command format! \n%1$s");
    }
}
