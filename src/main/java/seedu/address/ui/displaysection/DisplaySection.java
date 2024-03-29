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
import seedu.address.ui.UiPart;
import seedu.address.ui.personlistsection.AllContactsSection;

/**
 * Represents the display section in the user interface.
 * This section is responsible for displaying various lists of contacts,
 * such as all contacts, found contacts based on search criteria, and any custom list.
 * It allows for dynamic switching between these views via footer buttons.
 */
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

    /**
     * Initializes a DisplaySection with the logic layer of the application.
     * This constructor sets up the display sections and the footer buttons
     * that allow the user to switch between different views.
     *
     * @param logic The logic layer of the application, used to fetch contact lists and relationships.
     */
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

    /**
     * Displays the "All Contacts" section.
     * This method updates the view to show all contacts and sets the appropriate title.
     *
     * @param personLists The list of persons to be displayed.
     * @param relationships The list of relationships associated with the persons.
     */
    public void displayAllContactsSection(ObservableList<Person> personLists,
                                          ObservableList<Relationship> relationships) {
        footerButtonSection.selectAllContactButton();
        headerTitle.setText("All Contacts");
        allContactsSection.update(personLists, relationships);
        renderSection(allContactsSection.getRoot());
    }

    /**
     * Displays the "Found Contacts" section.
     * This method updates the view to show the results of a search operation and sets the appropriate title.
     *
     * @param personLists The list of persons found as a result of the search.
     * @param relationships The list of relationships associated with the found persons.
     */
    public void displayFoundResultSection(ObservableList<Person> personLists,
                                          ObservableList<Relationship> relationships) {
        footerButtonSection.selectFindResultButton();
        headerTitle.setText("Found Contacts");
        allContactsSection.update(personLists, relationships);
        renderSection(allContactsSection.getRoot());
    }

    /**
     * Displays a custom list section named "Any List".
     * This method allows for displaying any user-defined list of contacts, setting the appropriate title.
     *
     * @param personLists The list of persons to be displayed in the custom list.
     * @param relationships The list of relationships associated with the persons in the custom list.
     */
    public void displayAnyListSection(ObservableList<Person> personLists,
                                      ObservableList<Relationship> relationships) {
        footerButtonSection.selectAnyListButton();
        headerTitle.setText("Any List");
        allContactsSection.update(personLists, relationships);
        renderSection(allContactsSection.getRoot());
    }

    /**
     * Renders the given UI part within the body of the display section.
     * This method clears any existing content in the body and displays the new content.
     *
     * @param section The UI part to be displayed in the body.
     */
    private void renderSection(Node section) {
        body.getChildren().clear();
        body.getChildren().add(section);
    }
}
