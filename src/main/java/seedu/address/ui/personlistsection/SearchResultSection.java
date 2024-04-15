package seedu.address.ui.personlistsection;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.ui.PersonList;
import seedu.address.ui.UiPart;

/**
 * A subsection within the displaySection that display All contacts
 */
public class SearchResultSection extends UiPart<Region> {
    private static final String FXML = "person-list-section/SearchResultSection.fxml";
    private PersonList personList;
    private TreeMapFlowPane treeMapFlowPane;
    @FXML
    private VBox personListPlaceHolder;
    @FXML
    private VBox treeMapPlaceHolder;

    /**
     * Instantiates a anyListSection.
     */
    public SearchResultSection() {
        super(FXML);
        init();
    }
    /**
     * Updates the sorted modules in the ModuleListSection.
     *
     */
    public void update(ObservableList<Person> personList, ObservableList<Relationship> relationships) {
        this.personList.setPersonListCardItems(personList, relationships, "\"This Guy is an island, "
                + "cause he has no Relationships\"");
        if (relationships.size() == 0) {
            treeMapPlaceHolder.setVisible(false);
            treeMapPlaceHolder.setManaged(false);
        } else {
            treeMapPlaceHolder.setVisible(true);
            treeMapPlaceHolder.setManaged(true);
            this.treeMapFlowPane.updateFlowPane(personList, relationships);
        }
    }
    public void init() {
        this.personListPlaceHolder.getChildren().clear();
        this.treeMapPlaceHolder.getChildren().clear();
        this.personList = new PersonList("You Have To Use AnySearch First");
        this.treeMapFlowPane = new TreeMapFlowPane();
        personListPlaceHolder.getChildren().add(this.personList.getRoot());
        treeMapPlaceHolder.getChildren().add(this.treeMapFlowPane.getRoot());
        treeMapPlaceHolder.setVisible(false);
        treeMapPlaceHolder.setManaged(false);
    }
}


