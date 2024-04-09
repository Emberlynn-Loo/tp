package seedu.address.model.util;

import java.util.UUID;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.attribute.Attribute;
import seedu.address.model.person.attribute.NameAttribute;
import seedu.address.model.person.attribute.PhoneNumberAttribute;
import seedu.address.model.person.attribute.StringAttribute;
import seedu.address.model.person.relationship.BioParentsRelationship;
import seedu.address.model.person.relationship.Relationship;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(UUID.fromString("00000000-0000-0000-0000-000000000001"), new Attribute[] {
                new NameAttribute("Name", "Alex Yeoh"),
                new PhoneNumberAttribute("Phone", 87438807),
                new StringAttribute("Address", "Blk 30 Geylang Street 29, #06-40")
            }),
            new Person(UUID.fromString("00000000-0000-0000-0000-000000000002"), new Attribute[] {
                new NameAttribute("Name", "Bernice Yu"),
                new PhoneNumberAttribute("Phone", 99272758),
                new StringAttribute("Email", "berniceyu@example.com"),
                new StringAttribute("Address", "Blk 30 Lorong 3 Serangoon Gardens, #07-18")
            }),
            new Person(UUID.fromString("00000000-0000-0000-0000-000000000003"), new Attribute[] {
                new NameAttribute("Name", "Charlotte Oliveiro"),
                new PhoneNumberAttribute("Phone", 93210283),
                new StringAttribute("Email", "charlotte@example.com"),
                new StringAttribute("Address", "Blk 11 Ang Mo Kio Street 74, #11-04")
            }),
            new Person(UUID.fromString("00000000-0000-0000-0000-000000000004"), new Attribute[] {
                new NameAttribute("Name", "David Li"),
                new PhoneNumberAttribute("Phone", 91031282),
                new StringAttribute("Email", "lidavid@example.com"),
                new StringAttribute("Address", "Blk 436 Serangoon Gardens Street 26, #16-43")
            }),
            new Person(UUID.fromString("00000000-0000-0000-0000-000000000005"), new Attribute[] {
                new NameAttribute("Name", "Irfan Ibrahim"),
                new PhoneNumberAttribute("Phone", 92492021),
                new StringAttribute("Email", "irfan@example.com"),
                new StringAttribute("Address", "Blk 47 Tampines Street 20, #17-35")
            }),
            new Person(UUID.fromString("00000000-0000-0000-0000-000000000006"), new Attribute[] {
                new NameAttribute("Name", "Roy Balakrishnan"),
                new PhoneNumberAttribute("Phone", 92624417),
                new StringAttribute("Email", "royb@example.com"),
                new StringAttribute("Address", "Blk 45 Aljunied Street 85, #11-31")
            })
        };
    }

    public static Relationship[] getSampleRelationships() {
        return new Relationship[] {
            new Relationship(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                    UUID.fromString("00000000-0000-0000-0000-000000000002"), "friends"),
            new Relationship(UUID.fromString("00000000-0000-0000-0000-000000000003"),
                    UUID.fromString("00000000-0000-0000-0000-000000000004"), "colleagues"),
            new BioParentsRelationship(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                    UUID.fromString("00000000-0000-0000-0000-000000000003"), "parent", "child")
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Relationship sampleRelationship : getSampleRelationships()) {
            sampleAb.addRelationship(sampleRelationship);
        }
        return sampleAb;
    }

}
