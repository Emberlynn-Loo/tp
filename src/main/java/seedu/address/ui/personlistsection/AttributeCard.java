package seedu.address.ui.personlistsection;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.ui.UiPart;

/**
 * UI element to render each attribute key and value
 */
public class AttributeCard extends UiPart<Region> {
    private static final String FXML = "person-list-section/AttributeCard.fxml";
    @FXML
    private Label attributeKeyLabel;
    @FXML
    private Label attributeValueLabel;

    /**
     * constructor that takes in the attribute key and value string descriptors to be rendered
     * @param attributeKey
     * @param attributeValue
     */
    public AttributeCard(String attributeKey, String attributeValue) {
        super(FXML);
        attributeKeyLabel.setText(attributeKey);
        attributeValueLabel.setText(attributeValue);
    }
}
