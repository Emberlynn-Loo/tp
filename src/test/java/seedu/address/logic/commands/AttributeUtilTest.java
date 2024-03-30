package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.attribute.Attribute;

public class AttributeUtilTest {

    @Test
    public void createAttribute_validNameAttribute_returnsNameAttribute() throws CommandException {
        Attribute actualAttribute = AttributeUtil.createAttribute("Name", "John Doe");
        assertTrue(actualAttribute.getName().toString().equals("Name"));
    }

    @Test
    public void createAttribute_validPhoneAttribute_returnsPhoneAttribute() throws CommandException {
        Attribute actualAttribute = AttributeUtil.createAttribute("phone", "12345678");
        assertTrue(actualAttribute.getName().toString().equals("Phone"));
    }

    @Test
    public void createAttribute_validBirthdayAttribute_returnsBirthdayAttribute() throws CommandException {
        Attribute actualAttribute = AttributeUtil.createAttribute("Birthday", "2000-01-01");
        assertTrue(actualAttribute.getName().toString().equals("Birthday"));

    }

    @Test
    public void createAttribute_invalidPhoneAttribute_throwsCommandException() {
        assertThrows(CommandException.class, () -> AttributeUtil.createAttribute("phone", "not a number"));
    }

    @Test
    public void createAttribute_invalidBirthdayAttribute_throwsCommandException() {
        assertThrows(CommandException.class, () -> AttributeUtil.createAttribute("birthday", "not a date"));
    }
}
