package seedu.address.model.person.attribute;

/**
 * Name attribute with string value
 */
public class NameAttribute extends StringAttribute {
    /**
     * Constructor for NameAttribute
     * @param name name of the attribute
     * @param value value of the attribute
     */
    public NameAttribute(String name, String value) {
        super(name, value);
    }

    public String toString() {
        return value;
    }
}
