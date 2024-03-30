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

    public TreeMapFlowPane(ObservableList<Person> persons,
                           ObservableList<Relationship> relationships, ArrayList<String> descriptors) {
        super(FXML);
        addPersonTag("0001", "bioparent");
        treeMapPane.getChildren().add(new TreeEdgeComponent().getRoot());
        addPersonTag("0001", "bioparent");
        treeMapPane.getChildren().add(new TreeEdgeComponent().getRoot());
        addPersonTag("0001", "bioparent");
        treeMapPane.getChildren().add(new TreeEdgeComponent().getRoot());
        addPersonTag("0001", "bioparent");
        treeMapPane.getChildren().add(new TreeEdgeComponent().getRoot());
        addPersonTag("0001", "bioparent");
        treeMapPane.getChildren().add(new TreeEdgeComponent().getRoot());
    }

    private void addPersonTag(String personUuid, String tagStyle) {
        Label personTag = new Label(personUuid);
        personTag.getStyleClass().addAll("person-tag", "person-tag-" + tagStyle, "h3");
        treeMapPane.getChildren().add(personTag);
        /* Add a helper text to moduleCardTag to show full description of tag, on hover. */
        Tooltip relationshipToolTip = new Tooltip(personUuid);
        Tooltip.install(personTag, relationshipToolTip);
        relationshipToolTip.setShowDelay(Duration.seconds(0.05));
    }
    private class TreeEdgeComponent extends UiPart<Region> {
        private static final String FXML = "person-list-section/TreeEdgeComponent.fxml";
        @FXML
        private Label relationshipDescriptorLabel;
        private TreeEdgeComponent() {
            super(FXML);
            relationshipDescriptorLabel.setText("friend");
        }
    }
}