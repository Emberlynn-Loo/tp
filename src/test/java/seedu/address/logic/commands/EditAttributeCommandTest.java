package seedu.address.logic.commands;

import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class EditAttributeCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_null() {
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand(null, null);
        assertThrows(NullPointerException.class, () -> editAttributeCommand.execute(null));
    }

    @Test
    public void execute_pass() throws CommandException {
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand(ALICE.getUuidString().substring(32, 36),
                Map.of("Name", "Alice"));
        editAttributeCommand.execute(model);
        assert(ALICE.getAttribute("Name").toString().equals("Alice"));
    }

    @Test
    public void execute_pass_phoneNumber() throws CommandException {
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand(ALICE.getUuidString().substring(32, 36),
                Map.of("Phone", "12345678"));
        editAttributeCommand.execute(model);
        assert(ALICE.getAttribute("Phone").getValueAsString().equals("12345678"));
    }

    @Test
    public void execute_fail_attribute() {
        ALICE.deleteAttribute("Birthday");
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand(ALICE.getUuidString().substring(32, 36),
                Map.of("Birthday", "1999-01-01"));
        assertThrows(CommandException.class, () -> editAttributeCommand.execute(model));
    }

    @Test
    public void execute_fail_person() {
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand("0000",
                Map.of("Name", "Alice"));
        assertThrows(NullPointerException.class, () -> editAttributeCommand.execute(model));
    }

    @Test
    public void execute_null_person() {
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand(null, Map.of("Name", "Alice"));
        assertThrows(NullPointerException.class, () -> editAttributeCommand.execute(model));
    }

    @Test
    public void execute_pass_birthday() throws CommandException {
        ALICE.setAttribute("Birthday", "1999-01-02");
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand(ALICE.getUuidString().substring(32, 36),
                Map.of("Birthday", "1999-01-01"));
        editAttributeCommand.execute(model);
        assert(ALICE.getAttribute("Birthday").getValueAsString().equals("1999-01-01"));
    }

    @Test
    public void execute_fail_phoneNumber() {
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand(ALICE.getUuidString().substring(32, 36),
                Map.of("Phone", "-12345678"));
        assertThrows(CommandException.class, () -> editAttributeCommand.execute(model));
    }

    @Test
    public void execute_pass_randomAttribute() throws CommandException {
        ALICE.setAttribute("Random", "Random1");
        EditAttributeCommand editAttributeCommand = new EditAttributeCommand(ALICE.getUuidString().substring(32, 36),
                Map.of("Random", "Random"));
        editAttributeCommand.execute(model);
        assert(ALICE.getAttribute("Random").getValueAsString().equals("Random"));
    }

}
