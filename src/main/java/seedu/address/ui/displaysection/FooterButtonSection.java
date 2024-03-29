package seedu.address.ui.displaysection;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import seedu.address.ui.UiPart;

/**
 * A group of buttons in the footer of the ResultsSection.
 */
public class FooterButtonSection extends UiPart<Region> {
    private static final String FXML = "display-section/NavBar.fxml";

    private FooterButton allContactButton;
    private FooterButton findResultButton;
    private FooterButton anyListButton;

    @FXML
    private HBox footerButtonSection;

    /**
     * Creates a FooterButtonSection with specific labels and handlers for each button.
     * @param allContactButtonLabel The text label of the allContactButton.
     * @param findResultButtonLabel The text label of the findResultButton.
     * @param anyListButtonLabel The text label of the anyListButton.
     * @param allContactButtonHandler The function to execute on clicking the allContactButton.
     * @param findResultButtonHandler The function to execute on clicking the findResultButton.
     * @param anyListButtonHandler The function to execute on clicking the anyListButton.
     */
    public FooterButtonSection(String allContactButtonLabel, String findResultButtonLabel, String anyListButtonLabel,
                               Runnable allContactButtonHandler, Runnable findResultButtonHandler,
                               Runnable anyListButtonHandler) {
        super(FXML);

        // Initialize the buttons with their labels and handlers
        allContactButton = new FooterButton(allContactButtonLabel, allContactButtonHandler);
        findResultButton = new FooterButton(findResultButtonLabel, findResultButtonHandler);
        anyListButton = new FooterButton(anyListButtonLabel, anyListButtonHandler);

        // Add buttons to the HBox, possibly with visual separators
        footerButtonSection.getChildren().addAll(allContactButton.getRoot(), getDeco(),
                findResultButton.getRoot(), getDeco(), anyListButton.getRoot());
    }

    /**
     * Updates the styles for all footer buttons for the case where allContact button gets selected.
     */
    public void selectAllContactButton() {
        allContactButton.clearSelectedStyle();
        findResultButton.clearSelectedStyle();
        anyListButton.clearSelectedStyle();
        allContactButton.addSelectedStyle();
    }

    /**
     * Updates the styles for all footer buttons for the case where findResult button gets selected.
     */
    public void selectFindResultButton() {
        allContactButton.clearSelectedStyle();
        findResultButton.clearSelectedStyle();
        anyListButton.clearSelectedStyle();
        findResultButton.addSelectedStyle();
    }

    /**
     * Updates the styles for all footer buttons for the case where anyList button gets selected.
     */
    public void selectAnyListButton() {
        allContactButton.clearSelectedStyle();
        findResultButton.clearSelectedStyle();
        anyListButton.clearSelectedStyle();
        anyListButton.addSelectedStyle();
    }

    /**
     * Creates a decoration (a circle) to separate each button in the group.
     */
    private Circle getDeco() {
        Circle deco = new Circle();
        deco.setRadius(3);
        deco.getStyleClass().add("footer-buttons-deco");
        return deco;
    }
}
