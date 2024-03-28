package seedu.address.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonList extends UiPart<Region> {
    private static final String FXML = "PersonListSection/PersonList.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonList.class);
    @FXML
    private VBox personListCardVbox;
    @FXML
    private ScrollPane personListScrollPane;
    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonList(ObservableList<Person> personList) {
        super(FXML);
        setPersonListCardItems(personList);
    }
    private void setPersonListCardItems(ObservableList<Person> personList) {
        personListCardVbox.getChildren().clear();
        personListScrollPane.setVvalue(0);
        for (Person p: personList) {
            personListCardVbox.getChildren().add(new PersonCard(p).getRoot());
        }
    }
}
