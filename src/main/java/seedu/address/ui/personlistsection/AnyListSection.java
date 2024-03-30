package seedu.address.ui.personlistsection;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.ui.PersonList;
import seedu.address.ui.UiPart;

/**
 * A subsection within the displaySection that display All contacts
 */
public class AnyListSection extends UiPart<Region> {
    private static final String FXML = "person-list-section/AnyListSection.fxml";
    private PersonList personList;
    private TreeMapFlowPane treeMapFlowPane;
    @FXML
    private StackPane personListPlaceHolder;
    @FXML
    private StackPane treeMapPlaceHolder;

    /**
     * Instantiates a anyListSection.
     */
    public AnyListSection(ObservableList<Person> personList, ObservableList<Relationship> relationships,
                          ArrayList<String> relationshipDescriptors) {
        super(FXML);
        this.personList = new PersonList(personList, relationships);
        this.treeMapFlowPane = new TreeMapFlowPane(personList, relationships, relationshipDescriptors);
        personListPlaceHolder.getChildren().add(this.personList.getRoot());
        treeMapPlaceHolder.getChildren().add(this.treeMapFlowPane.getRoot());

    }
    /**
     * Updates the sorted modules in the ModuleListSection.
     *
     */
    public void update(ObservableList<Person> personList, ObservableList<Relationship> relationships,
                       ArrayList<String> relationshipDescriptors) {
        this.personList.setPersonListCardItems(personList, relationships);
    }
}

