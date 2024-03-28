package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seedu.address.model.person.Person;
import seedu.address.ui.personlistsection.AttributeCard;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "person-list-section/PersonCard.fxml";

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
    @FXML
    private FlowPane relationshipFlowPane;
    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person) {
        super(FXML);
        this.person = person;
        fillAttributesBox();
        personCardUuidLabel.setText(person.getUuid().toString().substring(32, 36));
        addRelationshipTag("Sibling", "purple");
    }

    private String displayAttributes() {
        return person.allAttributesAsString();
    }
    private void fillAttributesBox() {
        personCardAttributes.getChildren().clear();
        String[][] attributeStrings = person.allAttributesAsPairs();
        for (String[] strings: attributeStrings) {
            personCardAttributes.getChildren().add(new AttributeCard(strings[0], strings[1]).getRoot());
        }
    }
    /**
     * Adds a label to the ModuleCard that corresponds to a given tag.
     */
    private void addRelationshipTag(String relationshipDescriptor, String tagColor) {
        Label siblingTag = new Label("Sibling of B");
        siblingTag.getStyleClass().addAll("personCard-tag", "personCard-sibling", "h3");
        relationshipFlowPane.getChildren().add(siblingTag);
        /* Add a helper text to moduleCardTag to show full description of tag, on hover. */
        Tooltip siblingTagToolTip = new Tooltip("A is the Sibling of B");
        Tooltip.install(siblingTag, siblingTagToolTip);
        siblingTagToolTip.setShowDelay(Duration.seconds(0.05));

        Label bioparentTag = new Label("Bioparent of B");
        bioparentTag.getStyleClass().addAll("personCard-tag", "personCard-bioparent", "h3");
        relationshipFlowPane.getChildren().add(bioparentTag);
        Tooltip bioparentTagToolTip = new Tooltip("A is the Bioparent of B");
        Tooltip.install(bioparentTag, bioparentTagToolTip);
        bioparentTagToolTip.setShowDelay(Duration.seconds(0.05));

        Label spouseTag = new Label("Spouse of B");
        spouseTag.getStyleClass().addAll("personCard-tag", "personCard-spouse", "h3");
        relationshipFlowPane.getChildren().add(spouseTag);
        Tooltip spouseTagToolTip = new Tooltip("A is the Spouse of B");
        Tooltip.install(spouseTag, spouseTagToolTip);
        spouseTagToolTip.setShowDelay(Duration.seconds(0.05));
    }
}

