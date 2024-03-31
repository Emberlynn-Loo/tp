package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
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
    public PersonCard(Person person, ArrayList<Relationship> relationships) {
        super(FXML);
        this.person = person;
        personCardUuidLabel.setText(person.getUuid().toString().substring(32, 36));
        fillAttributesBox();
        if (relationships.size() == 0) {
            addEmptyRelationshipTag();
        } else {
            for (Relationship r: relationships) {
                String relationshipDescriptor = r.getRelativeRelationshipDescriptor(person.getUuid());
                String tagStyle = r.getStyleDescriptor();
                addRelationshipTag(relationshipDescriptor,
                        relationshipDescriptor, tagStyle);
            }
        }
    }
    private void fillAttributesBox() {
        personCardAttributes.getChildren().clear();
        String[][] attributeStrings = person.allAttributesAsPairs();
        if (attributeStrings == null) {
            setEmptyAttributeBox();
        } else {
            requireNonNull(attributeStrings);
            for (String[] s : attributeStrings) {
                personCardAttributes.getChildren().add(new AttributeCard(s[0], s[1]).getRoot());
            }
        }
    }
    public void setEmptyAttributeBox() {
        CustomPlaceholder emptyAttributePlaceholder =
                new CustomPlaceholder("No Attributes Found", 20);
        emptyAttributePlaceholder.setImageSize(100, 100);
        personCardAttributes.getChildren().add(emptyAttributePlaceholder.getRoot());
    }
    /**
     * Adds a label to the ModuleCard that corresponds to a given tag.
     */
    private void addRelationshipTag(String shortRelationshipDescriptor,
                                    String longRelationshipDescriptor, String tagStyle) {
        Label relationshipTag = new Label(shortRelationshipDescriptor);
        relationshipTag.getStyleClass().addAll("personCard-tag", "personCard-" + tagStyle, "h3");
        relationshipFlowPane.getChildren().add(relationshipTag);
        /* Add a helper text to moduleCardTag to show full description of tag, on hover. */
        Tooltip relationshipToolTip = new Tooltip(longRelationshipDescriptor);
        Tooltip.install(relationshipTag, relationshipToolTip);
        relationshipToolTip.setShowDelay(Duration.seconds(0.05));
    }
    private void addEmptyRelationshipTag() {
        Label relationshipTag = new Label("Person has no relations");
        relationshipTag.getStyleClass().addAll("personCard-tag", "personCard-empty", "h3");
        relationshipFlowPane.getChildren().add(relationshipTag);
        /* Add a helper text to moduleCardTag to show full description of tag, on hover. */
        Tooltip relationshipToolTip = new Tooltip("Start adding relations now");
        Tooltip.install(relationshipTag, relationshipToolTip);
        relationshipToolTip.setShowDelay(Duration.seconds(0.05));
    }
}

