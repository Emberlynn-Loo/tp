package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersonsUuid.ALICE;
import static seedu.address.testutil.TypicalPersonsUuid.BOB;
import static seedu.address.testutil.TypicalPersonsUuid.IDA;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.attribute.Attribute;
import seedu.address.model.person.attribute.NameAttribute;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

public class UniquePersonListTest {

    private final UniquePersonList uniquePersonList = new UniquePersonList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.contains(null));
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(uniquePersonList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        uniquePersonList.add(ALICE);
        assertTrue(uniquePersonList.contains(ALICE));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.add(null));
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.add(ALICE));
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPerson(null, ALICE));
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPerson(ALICE, null));
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.setPerson(ALICE, ALICE));
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(ALICE);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        uniquePersonList.add(ALICE);
        uniquePersonList.setPerson(ALICE, BOB);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniquePersonList.add(ALICE);
        uniquePersonList.add(BOB);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.setPerson(ALICE, BOB));
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.remove(null));
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        assertThrows(PersonNotFoundException.class, () -> uniquePersonList.remove(ALICE));
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        uniquePersonList.add(ALICE);
        uniquePersonList.remove(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPersons((UniquePersonList) null));
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        uniquePersonList.add(ALICE);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        uniquePersonList.setPersons(expectedUniquePersonList);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniquePersonList.setPersons((List<Person>) null));
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        uniquePersonList.add(ALICE);
        List<Person> personList = Collections.singletonList(BOB);
        uniquePersonList.setPersons(personList);
        UniquePersonList expectedUniquePersonList = new UniquePersonList();
        expectedUniquePersonList.add(BOB);
        assertEquals(expectedUniquePersonList, uniquePersonList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Person> listWithDuplicatePersons = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniquePersonList.setPersons(listWithDuplicatePersons));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniquePersonList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniquePersonList.asUnmodifiableObservableList().toString(), uniquePersonList.toString());
    }
    @Test
    void deleteAttribute_existingAttribute_removesAttribute() {
        assertTrue(IDA.hasAttribute("Name"));
        IDA.deleteAttribute("Name");
        assertFalse(IDA.hasAttribute("Name"));
    }

    @Test
    void getPersonByUuid_existingUuid_returnsPerson() {
        uniquePersonList.add(ALICE);
        Person foundPerson = uniquePersonList.getPersonByUuid(ALICE.getUuid());
        assertNotNull(foundPerson);
        assertEquals(ALICE.getUuid(), foundPerson.getUuid());
    }

    @Test
    void getFullUuid_matchingDigits_returnsFullUuid() {
        uniquePersonList.add(ALICE);
        String lastFourDigits = ALICE.getUuidString().substring(36 - 4);
        UUID foundUuid = uniquePersonList.getFullUuid(lastFourDigits);
        assertNotNull(foundUuid);
        assertEquals(ALICE.getUuid(), foundUuid);
    }

    @Test
    void hasAttribute_existingAttribute_returnsTrue() {
        uniquePersonList.add(ALICE);
        boolean result = uniquePersonList.hasAttribute(ALICE.getUuidString(), "Phone");
        assertTrue(result);
    }

    @Test
    void hasAttribute_nonExistingAttribute_returnsFalse() {
        boolean result = uniquePersonList.hasAttribute(ALICE.getUuidString(), "NonExistentAttribute");
        assertFalse(result);
    }

    @Test
    public void iterator() {
        UniquePersonList uniquePersonList = new UniquePersonList();
        Person person1 = new Person(new Attribute[0]);
        person1.updateAttribute(new NameAttribute("Name", "Amy Bee"));
        Person person2 = new Person(new Attribute[0]);
        person2.updateAttribute(new NameAttribute("Name", "Bob Charlie"));
        uniquePersonList.add(person1);
        uniquePersonList.add(person2);

        Iterator<Person> iterator = uniquePersonList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(person1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(person2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void equals() {
        UniquePersonList uniquePersonList = new UniquePersonList();
        Person person1 = new Person(new Attribute[0]);
        person1.updateAttribute(new NameAttribute("Name", "Amy Bee"));
        Person person2 = new Person(new Attribute[0]);
        person2.updateAttribute(new NameAttribute("Name", "Bob Charlie"));

        // same object -> returns true
        assertTrue(uniquePersonList.equals(uniquePersonList));

        // same values -> returns true
        UniquePersonList uniquePersonListCopy = new UniquePersonList();
        assertTrue(uniquePersonList.equals(uniquePersonListCopy));

        // different types -> returns false
        assertFalse(uniquePersonList.equals(1));

        // null -> returns false
        assertFalse(uniquePersonList.equals(null));

        // different person -> returns false
        uniquePersonList.add(person1);
        uniquePersonListCopy.add(person2);
        assertFalse(uniquePersonList.equals(uniquePersonListCopy));
    }

    @Test
    public void getPersonByUuid_nonExistingUuid_returnsNull() {
        UniquePersonList uniquePersonList = new UniquePersonList();
        Person person1 = new Person(new Attribute[0]);
        person1.updateAttribute(new NameAttribute("Name", "Amy Bee"));
        uniquePersonList.add(person1);

        UUID nonExistingUuid = UUID.randomUUID();

        while (uniquePersonList.getPersonByUuid(nonExistingUuid) != null) {
            nonExistingUuid = UUID.randomUUID();
        }

        Person result = uniquePersonList.getPersonByUuid(nonExistingUuid);
        assertNull(result);
    }
}
