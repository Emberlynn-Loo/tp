package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import seedu.address.ui.UiPart;
public class CustomPlaceholder extends UiPart<Region> {
    private static final String FXML = "/CustomPlaceholder.fxml";
    @FXML
    private HBox placeholderContainer;
    @FXML
    private HBox leftDecoration;
    @FXML
    private HBox rightDecoration;
    @FXML
    private Label placeholderText;
    public CustomPlaceholder(String text, int fontSize) {
        super(FXML);
        placeholderText.setText(text);
        placeholderText.setFont(new Font("Raleway", fontSize));
    }
}
