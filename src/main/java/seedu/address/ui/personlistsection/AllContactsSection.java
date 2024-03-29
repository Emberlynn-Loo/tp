package seedu.address.ui.personlistsection;

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
public class AllContactsSection extends UiPart<Region> {
    private static final String FXML = "person-list-section/AllContactSection.fxml";
    private PersonList personList;
    @FXML
    private StackPane personListPlaceholder;

    /**
     * Instantiates a ModuleSection.
     */
    public AllContactsSection(ObservableList<Person> personList, ObservableList<Relationship> relationships) {
        super(FXML);
        this.personList = new PersonList(personList, relationships);
        personListPlaceholder.getChildren().add(this.personList.getRoot());
    }
    /**
     * Updates the sorted modules in the ModuleListSection.
     *
     */
    public void update(ObservableList<Person> personList, ObservableList<Relationship> relationships) {
        this.personList.setPersonListCardItems(personList, relationships);
    }
}





