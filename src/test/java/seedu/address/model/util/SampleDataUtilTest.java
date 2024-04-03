package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();
        assertNotNull(samplePersons, "Sample persons should not be null");
        assertTrue(samplePersons.length > 0, "Sample persons should not be empty");
    }

    @Test
    public void getSampleRelationships() {
        Relationship[] sampleRelationships = SampleDataUtil.getSampleRelationships();
        assertNotNull(sampleRelationships, "Sample relationships should not be null");
        assertTrue(sampleRelationships.length > 0, "Sample relationships should not be empty");
    }

    @Test
    public void getSampleAddressBook() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();
        assertNotNull(sampleAddressBook, "Sample address book should not be null");
        assertTrue(sampleAddressBook.getPersonList().size() > 0,
                "Sample address book should contain persons");
        assertTrue(sampleAddressBook.getRelationshipList().size() > 0,
                "Sample address book should contain relationships");
    }
}
