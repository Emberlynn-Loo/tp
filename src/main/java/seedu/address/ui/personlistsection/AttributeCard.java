package seedu.address.ui.personlistsection;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.ui.UiPart;

public class AttributeCard extends UiPart<Region> {
    private static final String FXML = "person-list-section/AttributeCard.fxml";
    @FXML
    private Label attributeKeyLabel;
    @FXML
    private Label attributeValueLabel;

    public AttributeCard(String attributeKey, String attributeValue) {
        super(FXML);
        attributeKeyLabel.setText(attributeKey);
        attributeValueLabel.setText(attributeValue);
    }
}
