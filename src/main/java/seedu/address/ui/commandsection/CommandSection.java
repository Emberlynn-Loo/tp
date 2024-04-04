package seedu.address.ui.commandsection;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.UiPart;

/**
 * A subsection within the displaySection that display All contacts
 */
public class CommandSection extends UiPart<Region> {
    private static final String FXML = "command-section/CommandSection.fxml";
    private static final String ERROR = "failure-text";
    private static final Image SUCCESS = new Image("/images/command-success.png");
    private static final Image FAILURE = new Image("/images/command-error.png");
    private static final Image WELCOME = new Image("./images/welcome.png");
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
    @FXML
    private ImageView commandBoxImageContainer;
    private CommandExecutor commandExecutor;
    private ArrayList<String> pastCommands = new ArrayList<>();
    private int pastCommandIndex = 0;

    /**
     * Instantiates a anyListSection.
     */
    public CommandSection(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        commandBoxImageContainer.setVisible(false);
        commandBoxImageContainer.setManaged(false);
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
        addWelcomeDialog();
        commandSectionDialogContainer.heightProperty().addListener((observable) ->
                commandSectionDialogScrollPane.setVvalue(1.0));
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
        if (commandText.equalsIgnoreCase("c") || commandText.equalsIgnoreCase("clear")) {
            commandSectionDialogContainer.getChildren().clear();
            commandBoxImageContainer.setVisible(false);
            commandBoxImageContainer.setManaged(false);
            addWelcomeDialog();
            cliInput.setText("");
            return;
        }
        removeWelcomeDialog();
        commandBoxImageContainer.setVisible(true);
        commandBoxImageContainer.setManaged(true);
        if (pastCommands.size() == 0 || !commandText.equalsIgnoreCase(pastCommands.get(pastCommands.size() - 1))) {
            pastCommands.add(commandText);
        }
        pastCommandIndex = pastCommands.size();
        try {
            CommandResult commandResult = commandExecutor.execute(commandText);
            setDialogLabel(commandResult.getFeedbackToUser(), true);
            cliInput.setText("");
            setStyleToDefault();
            commandBoxImageContainer.setImage(SUCCESS);
        } catch (CommandException | ParseException e) {
            setDialogLabel(e.getMessage(), false);
            setStyleToIndicateCommandFailure();
            commandBoxImageContainer.setImage(FAILURE);
        }
    }
    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        cliInput.getStyleClass().remove(ERROR);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
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
    private void addWelcomeDialog() {
        Label dialogLabel = new Label("Hello From Command Section");
        dialogLabel.setId("command-section_dialog-label-welcome");
        dialogLabel.setWrapText(true);
        dialogLabel.prefWidthProperty().bind(commandSectionDialogContainer.widthProperty());
        ImageView welcomeImage = new ImageView();
        welcomeImage.setFitWidth(200);
        welcomeImage.setFitHeight(200);
        welcomeImage.setPreserveRatio(true);
        welcomeImage.setImage(WELCOME);
        welcomeImage.setId("welcome-image");
        commandSectionDialogContainer.setAlignment(Pos.CENTER);
        commandSectionDialogContainer.getChildren().add(dialogLabel);
        commandSectionDialogContainer.getChildren().add(welcomeImage);
    }
    private void removeWelcomeDialog() {
        commandSectionDialogContainer.getChildren().removeAll(
                commandSectionDialogContainer.lookup("#command-section_dialog-label-welcome"),
                commandSectionDialogContainer.lookup("#welcome-image"));
        commandSectionDialogContainer.setAlignment(Pos.TOP_LEFT);
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






