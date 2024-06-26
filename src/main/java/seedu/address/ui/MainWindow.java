package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.person.relationship.Relationship;
import seedu.address.ui.commandsection.CommandSection;
import seedu.address.ui.displaysection.DisplaySection;
import seedu.address.ui.displaysection.NavBar;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private DisplaySection displaySection;
    private CommandSection commandSection;
    private HelpWindow helpWindow;
    private NavBar navBar;
    @FXML
    private VBox mainWindowContainer;
    @FXML
    private HBox mainWindowNavBar;
    @FXML
    private HBox mainWindowNavBarButtonPlaceholder;
    @FXML
    private HBox mainWindowBody;
    @FXML
    private VBox displaySectionPlaceholder;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private StackPane statusbarPlaceholder;
    @FXML
    private VBox commandSectionPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }
    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        displaySection = new DisplaySection(logic);
        displayNavBar("All Contacts", "Search Results", () ->
                displayAllContactsSection(), () ->
                displaySearchResultSection());
        displaySectionPlaceholder.getChildren().add(displaySection.getRoot());
        commandSectionPlaceholder.getChildren().add(new CommandSection(this::executeCommand).getRoot());
        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());
        displayAllContactsSection(logic.getFilteredPersonList(), logic.getRelationshipList());
    }
    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }
    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }
    /**
     * Displays the footer buttons that enables user to toggle between different sections.
     * @param allContactButtonLabel The text label of the all contacts button.
     * @param searchResultButtonLabel The text label of search Result button.
     * @param allContactButtonHandler The function to execute on clicking the all contacts button.
     * @param anyListButtonHandler The function to execute on clicking the anyList button.
     */
    private void displayNavBar(String allContactButtonLabel, String searchResultButtonLabel,
                               Runnable allContactButtonHandler, Runnable anyListButtonHandler) {
        this.navBar = new NavBar(
                allContactButtonLabel, searchResultButtonLabel,
                allContactButtonHandler, anyListButtonHandler);
        mainWindowNavBarButtonPlaceholder.getChildren().remove(
                mainWindowNavBarButtonPlaceholder.lookup(".nav-button-section"));
        mainWindowNavBarButtonPlaceholder.getChildren().add(navBar.getRoot());
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
        navBar.selectAllContactButton();
        displaySection.displayAllContactsSection(personLists, relationships);
    }

    /**
     * navigates to existing contactSection with no rendering
     */
    public void displayAllContactsSection() {
        navBar.selectAllContactButton();
        displaySection.displayAllContactsSection();
    }

    /**
     * reset SearchResultSection after every change to Model
     */
    public void resetSearchResultSection() {
        displaySection.resetSearchResultSection();
    }
    /**
     * Displays a custom list section named "Any List".
     * This method allows for displaying any user-defined list of contacts, setting the appropriate title.
     */
    public void displaySearchResultSection() {
        navBar.selectSearchResultButton();
        displaySection.displaySearchResultSection();
    }

    /**
     * navaigates and updates the anyListSection with results from anySearch Command
     * @param persons persons in relationshipPathway if there is any
     * @param relationships relationships in relationshipPathway if there is any
     */
    public void displayUpdatedSearchResultSection(ObservableList<Person> persons,
                                                  ObservableList<Relationship> relationships) {
        navBar.selectSearchResultButton();
        displaySection.displayUpdatedSearchResultSection(persons, relationships);
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            if (commandResult.isAnySearch()) {
                displayUpdatedSearchResultSection(logic.getFilteredPersonList(), logic.getRelationshipList());
            } else {
                resetSearchResultSection();
                displayAllContactsSection(logic.getFilteredPersonList(), logic.getRelationshipList());
            }
            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }
            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            throw e;
        }
    }
}
