package seedu.address.ui.commandsection;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.ui.CommandBox;
import seedu.address.ui.UiPart;

/**
 * A subsection within the displaySection that display All contacts
 */
public class CommandSection extends UiPart<Region> {
    private static final String FXML = "command-section/CommandSection.fxml";
    @FXML
    private VBox commandSectionContainer;
    @FXML
    private VBox commandSectionDialogContainer;
    @FXML
    private VBox commandBoxContainer;
    @FXML
    private ScrollPane commandSectionDialogScrollPane;
    @FXML
    private TextField cliInput;

    /**
     * Instantiates a anyListSection.
     */
    public CommandSection(CommandExecutor commandExecutor) {
        super(FXML);
        setDialogLabel("hello from CommandSection");
    }
    /**
     * Updates the sorted modules in the ModuleListSection.
     *
     */
    public void update(ObservableList<Person> personList, ObservableList<Relationship> relationships) {

    }
    private void setDialogLabel(String text) {
        Label dialogLabel = new Label(text);
        dialogLabel.getStyleClass().add("command-section_dialog-label");
        dialogLabel.setWrapText(true);
        commandSectionDialogContainer.getChildren().add(dialogLabel);
    }
    @FXML
    public void handleCommandEntered() {

    }
    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }
}


