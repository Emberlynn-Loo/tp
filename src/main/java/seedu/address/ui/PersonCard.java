package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.ui.personlistsection.AttributeCard;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListSection/PersonCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */
    public final Person person;
    @FXML
    private Label personCardUuidLabel;
    @FXML
    private VBox personCardAttributes;
    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person) {
        super(FXML);
        this.person = person;
        fillAttributesBox();
        personCardUuidLabel.setText(person.getUuid().toString().substring(32, 36));
    }

    private String displayAttributes() {
        return person.allAttributesAsString();
    }
    private void fillAttributesBox() {
        personCardAttributes.getChildren().clear();
        personCardAttributes.getChildren().add(new AttributeCard("Name", "SpongeBob").getRoot());
        personCardAttributes.getChildren().add(new AttributeCard("Address", "Under the sea Spongebob squarepatns spongebob spoognebob spongendnenenene").getRoot());
    }
}

