package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.attribute.Attribute;
import seedu.address.model.person.attribute.NameAttribute;
import seedu.address.model.person.attribute.PhoneNumberAttribute;
import seedu.address.model.person.attribute.StringAttribute;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Attribute[] {
                new NameAttribute("Name", "Alex Yeoh"),
                new PhoneNumberAttribute("Phone", 87438807),
                new StringAttribute("Address", "Blk 30 Geylang Street 29, #06-40")
            }),
            new Person(new Attribute[] {
                new NameAttribute("Name", "Bernice Yu"),
                new PhoneNumberAttribute("Phone", 99272758),
                new StringAttribute("Email", "berniceyu@example.com"),
                new StringAttribute("Address", "Blk 30 Lorong 3 Serangoon Gardens, #07-18")
            }),
            new Person(new Attribute[] {
                new NameAttribute("Name", "Charlotte Oliveiro"),
                new PhoneNumberAttribute("Phone", 93210283),
                new StringAttribute("Email", "charlotte@example.com"),
                new StringAttribute("Address", "Blk 11 Ang Mo Kio Street 74, #11-04")
            }),
            new Person(new Attribute[] {
                new NameAttribute("Name", "David Li"),
                new PhoneNumberAttribute("Phone", 91031282),
                new StringAttribute("Email", "lidavid@example.com"),
                new StringAttribute("Address", "Blk 436 Serangoon Gardens Street 26, #16-43")
            }),
            new Person(new Attribute[] {
                new NameAttribute("Name", "Irfan Ibrahim"),
                new PhoneNumberAttribute("Phone", 92492021),
                new StringAttribute("Email", "irfan@example.com"),
                new StringAttribute("Address", "Blk 47 Tampines Street 20, #17-35")
            }),
            new Person(new Attribute[] {
                new NameAttribute("Name", "Roy Balakrishnan"),
                new PhoneNumberAttribute("Phone", 92624417),
                new StringAttribute("Email", "royb@example.com"),
                new StringAttribute("Address", "Blk 45 Aljunied Street 85, #11-31")
            })
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
