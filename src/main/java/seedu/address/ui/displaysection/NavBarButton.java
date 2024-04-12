package seedu.address.ui.displaysection;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import seedu.address.ui.UiPart;

/**
 * A button in the footer of the ResultsSection.
 */
public class NavBarButton extends UiPart<Region> {
    private static final String FXML = "display-section/NavBarButton.fxml";
    @FXML
    private VBox navButtonContainer;
    @FXML
    private Button navButton;
    /**
     * Creates a Footer button component.
     * @param label The label of the button.
     * @param handler The function to be executed on clicking the button.
     */
    public NavBarButton(String label, Runnable handler) {
        super(FXML);

        navButton.setText(label);
        navButton.setOnAction((event) -> {
            handler.run();
        });
    }

    /**
     * Adds the selected style of the button.
     */
    public void addSelectedStyle() {
        navButtonContainer.getStyleClass().add("nav-button-container-selected");
        navButton.getStyleClass().add("nav-button-selected");
        Circle deco = new Circle();
        deco.setRadius(3);
        deco.getStyleClass().add("nav-button-selected-deco");
        navButtonContainer.getChildren().add(deco);
    }

    /**
     * Clears the selected style of the button.
     */
    public void clearSelectedStyle() {
        navButton.getStyleClass().remove("nav-button-selected");
        navButtonContainer.getStyleClass().remove("nav-button-container-selected");
        navButtonContainer.getChildren()
                .remove(navButtonContainer.lookup(".nav-button-selected-deco"));
    }
}

