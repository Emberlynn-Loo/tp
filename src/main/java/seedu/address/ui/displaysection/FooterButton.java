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
public class FooterButton extends UiPart<Region> {
    private static final String FXML = "display-section/NavBarButton.fxml";
    @FXML
    private VBox footerButtonContainer;
    @FXML
    private Button footerButton;
    /**
     * Creates a Footer button component.
     * @param label The label of the button.
     * @param handler The function to be executed on clicking the button.
     */
    public FooterButton(String label, Runnable handler) {
        super(FXML);

        footerButton.setText(label);
        footerButton.setOnAction((event) -> {
            handler.run();
        });
    }

    /**
     * Adds the selected style of the button.
     */
    public void addSelectedStyle() {
        footerButtonContainer.getStyleClass().add("footer-button-container-selected");
        footerButton.getStyleClass().add("footer-button-selected");
        Circle deco = new Circle();
        deco.setRadius(3);
        deco.getStyleClass().add("footer-button-selected-deco");
        footerButtonContainer.getChildren().add(deco);
    }

    /**
     * Clears the selected style of the button.
     */
    public void clearSelectedStyle() {
        footerButton.getStyleClass().remove("footer-button-selected");
        footerButtonContainer.getStyleClass().remove("footer-button-container-selected");
        footerButtonContainer.getChildren()
                .remove(footerButtonContainer.lookup(".footer-button-selected-deco"));
    }
}

