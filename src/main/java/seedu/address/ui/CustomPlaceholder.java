package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

/**
 * Represents a custom placeholder with decorations and text.
 * This can be used in scenarios where a UI element needs to be visually represented as a placeholder
 * before being replaced or filled with actual content.
 * <p>
 * The class supports customizations for the text and its font size, and includes left and right decorations.
 */
public class CustomPlaceholder extends UiPart<Region> {
    private static final String FXML = "CustomPlaceHolder.fxml";

    @FXML
    private HBox placeholderContainer;

    @FXML
    private HBox leftDecoration;

    @FXML
    private HBox rightDecoration;

    @FXML
    private Label placeholderText;
    @FXML
    private ImageView imageContainer;

    /**
     * Creates an instance of CustomPlaceholder with specified text and font size.
     *
     * @param text     The text to be displayed in the placeholder.
     * @param fontSize The font size of the text.
     */
    public CustomPlaceholder(String text, int fontSize) {
        super(FXML);
        placeholderText.setText(text);
        placeholderText.setFont(new Font("Raleway", fontSize));
    }
    public void setImageSize(int width, int height) {
        imageContainer.setFitWidth(width);
        imageContainer.setFitHeight(height);
        imageContainer.setPreserveRatio(true);
    }
}
