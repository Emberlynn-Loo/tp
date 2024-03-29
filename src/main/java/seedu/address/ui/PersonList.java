package seedu.address.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;

/**
 * Panel containing the list of persons.
 */
public class PersonList extends UiPart<Region> {
    private static final String FXML = "person-list-section/PersonList.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonList.class);
    @FXML
    private VBox personListCardVbox;
    @FXML
    private ScrollPane personListScrollPane;
    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonList(ObservableList<Person> personList, ObservableList<Relationship> relationships) {
        super(FXML);
        setPersonListCardItems(personList, relationships);
    }
    public void setPersonListCardItems(ObservableList<Person> personList, ObservableList<Relationship> relationships) {
        personListCardVbox.getChildren().clear();
        personListScrollPane.setVvalue(0);
        if (personList.size() == 0) {
            displayPlaceholderText("You Have No Contacts, Start Adding Them!");
            return;
        }
        for (Person p: personList) {
            ArrayList<Relationship> personRelationships = new ArrayList<>();
            for (Relationship r : relationships) {
                if (r.containsUuid(p.getUuid()) != null) {
                    personRelationships.add(r);
                }
            }
            personListCardVbox.getChildren().add(new PersonCard(p, personRelationships).getRoot());
        }
    }
    private void displayPlaceholderText(String text) {
        CustomPlaceholder customPlaceholder = new CustomPlaceholder(
                "You Have No Contacts, Start Adding Them!", 20);
        personListCardVbox.getChildren().add(customPlaceholder.getRoot());
    }
}
