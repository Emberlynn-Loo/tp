package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.attribute.Attribute;
import seedu.address.model.person.attribute.BirthdayAttribute;
import seedu.address.model.person.attribute.NameAttribute;
import seedu.address.model.person.attribute.StringAttribute;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void equals() {

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(Integer.parseInt(VALID_PHONE_BOB)).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{uuid=" + ALICE.getUuidString()
                + "}";
        assertEquals(expected, ALICE.toString());
    }
    @Test
    public void getName() {
        assertEquals(VALID_NAME_BOB, BOB.getAttribute("Name").toString());
    }

    @Test
    public void getUuidString() {
        Person person = new PersonBuilder().build();
        String personUuidString = person.getUuidString();
        // Solution below adapted from Github Copilot
        String uuidFormat = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
        assertTrue(personUuidString.matches(uuidFormat));
    }
    @Test
    public void getUuidString_differentUuids() {
        Person person1 = new PersonBuilder().build();
        Person person2 = new PersonBuilder().build();
        assertFalse(person1.getUuidString().equals(person2.getUuidString()));
    }
    @Test
    public void equalsUuid_samePerson_true() {
        Person person1 = new PersonBuilder().build();
        assertTrue(person1.equalsUuid(person1));
    }
    @Test
    public void equalsUuid_differentUuids_false() {
        Person person1 = new PersonBuilder().build();
        Person person2 = new PersonBuilder().build();
        assertFalse(person1.equalsUuid(person2));
    }
    @Test
    public void equalsUuid_notPerson_false() {
        Person person1 = new PersonBuilder().build();
        assertFalse(person1.equalsUuid("not a person"));
    }
    @Test
    public void getUuid() {
        assertTrue(ALICE.getUuid() != null);
    }
    @Test
    public void hasAttribute_noAttribute_false() {
        assertFalse(ALICE.hasAttribute("no attribute"));
    }
    @Test
    public void hasAttribute_hasAttribute_true() {
        assertTrue(ALICE.hasAttribute("Name"));
    }
    @Test
    public void getAttribute_noAttribute_null() {
        try {
            ALICE.getAttribute("no attribute");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    @Test
    public void getAttribute_hasAttribute() {
        assertTrue(ALICE.getAttribute("Name") != null);
    }
    @Test
    public void getAttribute_noAttribute() {
        try {
            ALICE.getAttribute("no attribute");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    @Test
    public void getAttribute_hasAttribute_correctAttribute() {
        assertTrue(ALICE.getAttribute("Name").getName().equals("Name"));
    }
    @Test public void getAttribute_invalidName() {
        try {
            ALICE.getAttribute("");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    @Test
    public void updateAttribute_noAttribute() {
        try {
            Person aliceCopy = new PersonBuilder(ALICE).build();
            aliceCopy.updateAttribute(new StringAttribute("no attribute", "new value"));
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    @Test
    public void updateAttribute_hasAttribute() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        aliceCopy.updateAttribute(new NameAttribute("Name", "new value"));
        assertEquals("new value", aliceCopy.getAttribute("Name").getValueAsString());
    }
    @Test
    public void updateAttribute_invalidName() {
        try {
            Person aliceCopy = new PersonBuilder(ALICE).build();
            aliceCopy.updateAttribute(new StringAttribute("", "new value"));
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    @Test
    public void deleteAttribute_noAttribute() {
        try {
            Person aliceCopy = new PersonBuilder(ALICE).build();
            aliceCopy.deleteAttribute("no attribute");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }
    @Test
    public void deleteAttribute_hasAttribute() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        aliceCopy.deleteAttribute("Name");
        assertFalse(aliceCopy.hasAttribute("Name"));
    }
    @Test
    public void person_nameAttribute() {
        NameAttribute name = new NameAttribute("Name", "Alice Pauline");
        BirthdayAttribute birthday = new BirthdayAttribute(
                "Birthday",
                LocalDate.of(1999, 1, 1));
        Person aliceCopy = new Person(new Attribute[]{name, birthday});

        assertEquals("Alice Pauline", aliceCopy.getAttribute("Name").getValueAsString());
        assertEquals(
                LocalDate.of(1999, 1, 1).toString(),
                aliceCopy.getAttribute("Birthday").getValueAsString());
    }

    @Test
    public void allAttributesAsString() {
        NameAttribute name = new NameAttribute("Name", "Alice Pauline");
        BirthdayAttribute birthday = new BirthdayAttribute(
                "Birthday",
                LocalDate.of(1999, 1, 1));
        Person aliceCopy = new Person(new Attribute[]{name, birthday});
        assertEquals(
                "Name: Alice Pauline\nBirthday: 1999-01-01",
                aliceCopy.allAttributesAsString());
    }
    @Test
    public void allAttributesAsPairs() {
        NameAttribute name = new NameAttribute("Name", "Alice Pauline");
        BirthdayAttribute birthday = new BirthdayAttribute(
                "Birthday",
                LocalDate.of(1999, 1, 1));
        Person aliceCopy = new Person(new Attribute[]{name, birthday});
        String[][] expected = new String[2][2];
        expected[1] = new String[] {"Name:", "Alice Pauline"};
        expected[0] = new String[] {"Birthday:", "1999-01-01"};
        for (String[] p : aliceCopy.allAttributesAsPairs()) {
            System.out.println(p[0]);
            System.out.println(p[1]);
        }
        assertArrayEquals(expected, aliceCopy.allAttributesAsPairs());
    }

    @Test
    public void deleteAttributeTest() {
        NameAttribute name = new NameAttribute("Name", "Alice Pauline");
        BirthdayAttribute birthday = new BirthdayAttribute(
                "Birthday",
                LocalDate.of(1999, 1, 1));
        Person aliceCopy = new Person(new Attribute[]{name, birthday});
        aliceCopy.deleteAttribute("Name");
        assertFalse(aliceCopy.hasAttribute("Name"));
    }
}
