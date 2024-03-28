package seedu.address.ui.displaysection;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.Logic;
import seedu.address.model.person.Person;
import seedu.address.ui.PersonList;
import seedu.address.ui.UiPart;

public class DisplaySection extends UiPart<Region> {
    private static final String FXML = "display-section/DisplaySection.fxml";
    private PersonList allContactsSection;
    @FXML
    private VBox displaySection;
    @FXML
    private Label headerTitle;
    @FXML
    private VBox body;

    public DisplaySection(Logic logic) {
        super(FXML);
        this.allContactsSection = new PersonList(logic.getFilteredPersonList());
        displayAllContactsSection();
    }
    private void displayAllContactsSection() {
        headerTitle.setText("All Contacts");
        body.getChildren().clear();
        body.getChildren().add(allContactsSection.getRoot());
    }
    private void renderSection(Node section) {
        body.getChildren().clear();
        body.getChildren().add(section);
    }
}
