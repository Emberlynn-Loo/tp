package seedu.address.ui.personlistsection;

import java.util.ArrayList;

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
 * UI element to render each attribute key and value
 */
public class TreeMapFlowPane extends UiPart<Region> {
    private static final String FXML = "person-list-section/TreeMapPane.fxml";
    @FXML
    private VBox treeMapPaneContainer;
    @FXML
    private ScrollPane treeMapPaneScrollPane;
    @FXML
    private FlowPane treeMapPane;

    public TreeMapFlowPane() {
        super(FXML);
        addEmptyPlaceholderText("Use Any Search First!");
    }
    private void addPersonTag(String personUuid) {
        Label personTag = new Label(personUuid);
        personTag.getStyleClass().addAll("person-tag", "h3");
        treeMapPane.getChildren().add(personTag);
        /* Add a helper text to moduleCardTag to show full description of tag, on hover. */
        Tooltip relationshipToolTip = new Tooltip(personUuid);
        Tooltip.install(personTag, relationshipToolTip);
        relationshipToolTip.setShowDelay(Duration.seconds(0.05));
    }
    private void addRelationshipEdge(String relationshipDescriptor, String tagStyle) {
        treeMapPane.getChildren().add(
                new TreeEdgeComponent(relationshipDescriptor, tagStyle).getRoot());
    }
    public void updateFlowPane(ObservableList<Person> persons, ObservableList<Relationship> relationships) {
        treeMapPane.getChildren().clear();
        if (persons.size() == 0 || relationships.size() == 0) {
            addEmptyPlaceholderText("This Guy is an island, cause he has no Relationships");
            return;
        }
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
    private class TreeEdgeComponent extends UiPart<Region> {
        private static final String FXML = "person-list-section/TreeEdgeComponent.fxml";
        @FXML
        private Label relationshipDescriptorLabel;
        private TreeEdgeComponent(String relationshipDescriptor, String tagStyle) {
            super(FXML);
            relationshipDescriptorLabel.setText(relationshipDescriptor);
            relationshipDescriptorLabel.getStyleClass().add(tagStyle);
        }
    }
    public void addEmptyPlaceholderText(String text) {
        treeMapPane.getChildren().clear();
        CustomPlaceholder customPlaceholder = new CustomPlaceholder(text, 20);
        treeMapPane.getChildren().add(customPlaceholder.getRoot());
    }
}
