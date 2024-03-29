package seedu.address.ui.displaysection;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.Logic;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.ui.PersonList;
import seedu.address.ui.UiPart;
import seedu.address.ui.personlistsection.AllContactsSection;

public class DisplaySection extends UiPart<Region> {
    private static final String FXML = "display-section/DisplaySection.fxml";
    private AllContactsSection allContactsSection;
    private FooterButtonSection footerButtonSection;
    @FXML
    private VBox displaySection;
    @FXML
    private Label headerTitle;
    @FXML
    private VBox body;

    public DisplaySection(Logic logic) {
        super(FXML);
        this.allContactsSection = new AllContactsSection(logic.getFilteredPersonList(), logic.getRelationshipList());
        displayFooter("All Contacts", "Found Contacts", "Any List", () ->
                displayAllContactsSection(logic.getFilteredPersonList(),
                        logic.getRelationshipList()), () ->
                displayFoundResultSection(logic.getFilteredPersonList(),
                        logic.getRelationshipList()), () ->
                displayAnyListSection(logic.getFilteredPersonList(),
                        logic.getRelationshipList()));
        displayAllContactsSection(logic.getFilteredPersonList(), logic.getRelationshipList());
    }
    private void displayAllContactsSection(ObservableList<Person> personLists,
                                           ObservableList<Relationship> relationships) {
        footerButtonSection.selectAllContactButton();
        headerTitle.setText("All Contacts");
        allContactsSection.update(personLists, relationships);
        renderSection(allContactsSection.getRoot());
    }
    private void displayFoundResultSection(ObservableList<Person> personLists,
                                           ObservableList<Relationship> relationships) {
        footerButtonSection.selectFindResultButton();
        headerTitle.setText("Found Contacts");
        allContactsSection.update(personLists, relationships);
        renderSection(allContactsSection.getRoot());
    }
    private void displayAnyListSection(ObservableList<Person> personLists,
                                       ObservableList<Relationship> relationships) {
        footerButtonSection.selectAnyListButton();
        headerTitle.setText("Any List");
        allContactsSection.update(personLists, relationships);
        renderSection(allContactsSection.getRoot());
    }
    /**
     * Displays the footer buttons that enables user to toggle between different sections.
     * @param allContactButtonLabel The text label of the all contacts button.
     * @param findResultButtonLabel The text label of the find result button.
     * @param anyListButtonLabel The text label of any list button.
     * @param allContactButtonHandler The function to execute on clicking the all contacts button.
     * @param findResultButtonHandler The function to execute on clicking the find result button.
     * @param anyListButtonHandler The function to execute on clicking the anyList button.
     */
    private void displayFooter(String allContactButtonLabel, String findResultButtonLabel, String anyListButtonLabel,
                              Runnable allContactButtonHandler, Runnable findResultButtonHandler,
                              Runnable anyListButtonHandler) {
        this.footerButtonSection = new FooterButtonSection(
                allContactButtonLabel, findResultButtonLabel, anyListButtonLabel,
                allContactButtonHandler, findResultButtonHandler, anyListButtonHandler);
        displaySection.getChildren().remove(displaySection.lookup(".footer-button-section"));
        displaySection.getChildren().add(footerButtonSection.getRoot());
    }
    private void renderSection(Node section) {
        body.getChildren().clear();
        body.getChildren().add(section);
    }
}

