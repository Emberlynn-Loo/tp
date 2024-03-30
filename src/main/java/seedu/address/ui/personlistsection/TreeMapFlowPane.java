package seedu.address.ui.personlistsection;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.ui.CustomPlaceholder;
import seedu.address.ui.UiPart;

/**
 * A UI component that displays a tree map representing persons and their relationships.
 * The tree map is visualized using a flow pane that dynamically adjusts to accommodate
 * the visual components representing persons and their relationships.
 */
public class TreeMapFlowPane extends UiPart<Region> {
    private static final String FXML = "person-list-section/TreeMapPane.fxml";

    @FXML
    private VBox treeMapPaneContainer;
    @FXML
    private ScrollPane treeMapPaneScrollPane;
    @FXML
    private FlowPane treeMapPane;

    /**
     * Initializes the tree map pane with default settings and placeholder text.
     */
    public TreeMapFlowPane() {
        super(FXML);
        addEmptyPlaceholderText("Use Any Search First!");
    }

    /**
     * Adds a visual tag for a person identified by their UUID to the tree map pane.
     * @param personUuid The UUID of the person to add, displayed as a tag.
     */
    private void addPersonTag(String personUuid) {
        Label personTag = new Label(personUuid);
        personTag.getStyleClass().addAll("person-tag", "h3");
        treeMapPane.getChildren().add(personTag);

        // Adds a tooltip to the tag to show the full UUID on hover.
        Tooltip relationshipToolTip = new Tooltip(personUuid);
        Tooltip.install(personTag, relationshipToolTip);
        relationshipToolTip.setShowDelay(Duration.seconds(0.05));
    }

    /**
     * Adds a visual representation of a relationship between two persons to the tree map pane.
     * @param relationshipDescriptor The descriptor of the relationship.
     * @param tagStyle The CSS style class to apply to the relationship visualization.
     */
    private void addRelationshipEdge(String relationshipDescriptor, String tagStyle) {
        treeMapPane.getChildren().add(
                new TreeEdgeComponent(relationshipDescriptor, tagStyle).getRoot());
    }

    /**
     * Updates the flow pane to display the persons and their relationships.
     * Clears existing content and repopulates with the provided persons and relationships.
     * @param persons The list of persons to display.
     * @param relationships The list of relationships to display.
     */
    public void updateFlowPane(ObservableList<Person> persons, ObservableList<Relationship> relationships) {
        treeMapPane.getChildren().clear();
        if (persons.isEmpty() || relationships.isEmpty()) {
            addEmptyPlaceholderText("This Guy is an island, cause he has no Relationships");
            return;
        }
        // Populates the pane with person tags and relationship edges.
        for (int i = 0; i < persons.size(); i++) {
            Person current = persons.get(i);
            if (i == persons.size() - 1) {
                addPersonTag(current.getLastFourCharacterOfUuid());
            } else {
                Relationship currentRelationship = relationships.get(i);
                addPersonTag(current.getLastFourCharacterOfUuid());
                addRelationshipEdge(
                        currentRelationship.getRelativeRelationshipDescriptorWithoutUuid(current.getUuid()),
                        currentRelationship.getStyleDescriptor());
            }
        }
    }

    /**
     * A private UI component representing an edge in the tree map, which visualizes a relationship.
     */
    private class TreeEdgeComponent extends UiPart<Region> {
        private static final String FXML = "person-list-section/TreeEdgeComponent.fxml";

        @FXML
        private Label relationshipDescriptorLabel;

        /**
         * Constructs a component representing a relationship edge with a descriptor and a visual style.
         * @param relationshipDescriptor The descriptor of the relationship.
         * @param tagStyle The CSS style class for the visual representation of the relationship.
         */
        private TreeEdgeComponent(String relationshipDescriptor, String tagStyle) {
            super(FXML);
            relationshipDescriptorLabel.setText(relationshipDescriptor);
            relationshipDescriptorLabel.getStyleClass().add(tagStyle);
        }
    }

    /**
     * Adds placeholder text to the tree map pane when it is empty.
     * @param text The placeholder text to display.
     */
    public void addEmptyPlaceholderText(String text) {
        treeMapPane.getChildren().clear();
        CustomPlaceholder customPlaceholder = new CustomPlaceholder(text, 20);
        treeMapPane.getChildren().add(customPlaceholder.getRoot());
    }
}
