package seedu.address.ui.commandsection;

import java.util.ArrayList;
import java.util.Queue;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private static final String ERROR = "failure-text";
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
    private CommandExecutor commandExecutor;
    private ArrayList<String> pastCommands = new ArrayList<>();
    private int pastCommandIndex = 0;

    /**
     * Instantiates a anyListSection.
     */
    public CommandSection(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        cliInput.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        // Add an event handler to the TextField
        cliInput.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.UP) {
                upArrowKeyListener();
            }
            if (event.getCode() == KeyCode.DOWN) {
                downArrowKeyListener();
            }
        });
        commandSectionDialogContainer.heightProperty().addListener(
                (observable) -> commandSectionDialogScrollPane.setVvalue(1.0));
        Platform.runLater(() -> cliInput.requestFocus()); // gooogle
    }
    private void setDialogLabel(String text, boolean success) {
        Label dialogLabel = new Label(text);
        dialogLabel.getStyleClass().add("command-section_dialog-label");
        dialogLabel.setWrapText(true);
        dialogLabel.prefWidthProperty().bind(commandSectionDialogContainer.widthProperty());
        if (success) {
            dialogLabel.getStyleClass().add("success");
        } else {
            dialogLabel.getStyleClass().add("failure");
        }
        commandSectionDialogContainer.getChildren().add(dialogLabel);
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = cliInput.getText();
        if (commandText.equals("")) {
            return;
        }
        if (commandText.equalsIgnoreCase("clr")) {
            commandSectionDialogContainer.getChildren().clear();
            cliInput.setText("");
            return;
        }
        pastCommands.add(commandText);
        pastCommandIndex = pastCommands.size();
        try {
            CommandResult commandResult = commandExecutor.execute(commandText);
            setDialogLabel(commandResult.getFeedbackToUser(), true);
            cliInput.setText("");
            setStyleToDefault();
        } catch (CommandException | ParseException e) {
            setDialogLabel(e.getMessage(), false);
            setStyleToIndicateCommandFailure();
        }
    }
    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault () {
        cliInput.getStyleClass().remove(ERROR);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure () {
        ObservableList<String> styleClass = cliInput.getStyleClass();

        if (styleClass.contains(ERROR)) {
            return;
        }
        styleClass.add(ERROR);
    }
    private void upArrowKeyListener() {
        if (pastCommands.size() == 0 || pastCommandIndex <= 0) {
            return;
        } else {
            pastCommandIndex -= 1;
            cliInput.setText(pastCommands.get(pastCommandIndex));
        }
    }
    private void downArrowKeyListener() {
        if (pastCommandIndex >= pastCommands.size()) {
            return;
        }
        pastCommandIndex += 1;
        if (pastCommandIndex == pastCommands.size()) {
            cliInput.setText("");
            return;
        }
        cliInput.setText(pastCommands.get(pastCommandIndex));
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






