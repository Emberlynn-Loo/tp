package seedu.address.testutil;

import java.util.UUID;

import seedu.address.model.person.Person;
import seedu.address.model.person.attribute.NameAttribute;
import seedu.address.model.person.attribute.PhoneNumberAttribute;
import seedu.address.model.person.attribute.StringAttribute;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final int DEFAULT_PHONE = 85355255;
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private NameAttribute name;
    private PhoneNumberAttribute phone;
    private StringAttribute email;
    private StringAttribute address;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new NameAttribute("Name", DEFAULT_NAME);
        phone = new PhoneNumberAttribute("Phone", DEFAULT_PHONE);
        email = new StringAttribute("Email", DEFAULT_EMAIL);
        address = new StringAttribute("Address", DEFAULT_ADDRESS);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = (NameAttribute) personToCopy.getAttribute("Name");
        phone = (PhoneNumberAttribute) personToCopy.getAttribute("Phone");
        email = (StringAttribute) personToCopy.getAttribute("Email");
        address = (StringAttribute) personToCopy.getAttribute("Address");
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new NameAttribute("Name", name);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new StringAttribute("Address", address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(int phone) {
        this.phone = new PhoneNumberAttribute("Phone", phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new StringAttribute("Email", email);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, address);
    }

    public Person buildWithUuid(String uuid) {
        return new Person(UUID.fromString(uuid), name, phone, email, address);
    }

}
