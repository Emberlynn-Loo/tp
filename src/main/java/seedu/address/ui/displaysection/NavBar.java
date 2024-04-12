package seedu.address.ui.displaysection;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import seedu.address.ui.UiPart;

/**
 * NavBar
 */
public class NavBar extends UiPart<Region> {
    private static final String FXML = "display-section/NavBar.fxml";
    private NavBarButton allContactButton;
    private NavBarButton searchResultButton;
    @FXML
    private HBox navBar;
    /**
     * Creates a NavBar with specific labels and handlers for each button.
     * @param allContactButtonLabel The text label of the allContactButton.
     * @param searchResultButtonLabel The text label of the searchResultButton.
     * @param allContactButtonHandler The function to execute on clicking the allContactButton.
     * @param searchResultButtonHandler The function to execute on clicking the searchResultButton.
     */
    public NavBar(String allContactButtonLabel, String searchResultButtonLabel,
                  Runnable allContactButtonHandler,
                  Runnable searchResultButtonHandler) {
        super(FXML);
        // Initialize the buttons with their labels and handlers
        allContactButton = new NavBarButton(allContactButtonLabel, allContactButtonHandler);
        searchResultButton = new NavBarButton(searchResultButtonLabel, searchResultButtonHandler);

        // Add buttons to the HBox, possibly with visual separators
        navBar.getChildren().addAll(allContactButton.getRoot(), searchResultButton.getRoot());
    }

    /**
     * Updates the styles for all footer buttons for the case where allContact button gets selected.
     */
    public void selectAllContactButton() {
        allContactButton.clearSelectedStyle();
        searchResultButton.clearSelectedStyle();
        allContactButton.addSelectedStyle();
    }
    /**
     * Updates the styles for all footer buttons for the case where anyList button gets selected.
     */
    public void selectSearchResultButton() {
        allContactButton.clearSelectedStyle();
        searchResultButton.clearSelectedStyle();
        searchResultButton.addSelectedStyle();
    }
}
