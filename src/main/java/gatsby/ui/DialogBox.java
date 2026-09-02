package gatsby.ui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/** Represents one speaker-labelled message in Gatsby's conversation. */
public class DialogBox extends HBox {
    /** Shows whether the message came from the user or Gatsby. */
    @FXML
    private Label speaker;

    /** Displays the message text. */
    @FXML
    private Label dialog;

    /** Displays Gatsby's avatar beside Gatsby's messages. */
    @FXML
    private ImageView gatsbyImage;

    /** Displays the user's avatar beside user messages. */
    @FXML
    private ImageView userImage;

    /** Loads the reusable dialog-box view and fills its text. */
    private DialogBox(String text, String speakerName, boolean isGatsby) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Gatsby's dialog layout", e);
        }

        speaker.setText(speakerName);
        dialog.setText(text);
        if (isGatsby) {
            gatsbyImage.setVisible(true);
            gatsbyImage.setManaged(true);
            flip();
        } else {
            userImage.setVisible(true);
            userImage.setManaged(true);
        }
    }

    /** Aligns a Gatsby message on the left side of the conversation. */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        getStyleClass().add("gatsby-dialog");
    }

    /**
     * Creates a right-aligned message containing user input.
     *
     * @param text the user's command
     * @return a dialog box for the user message
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, "You", false);
    }

    /**
     * Creates a left-aligned message containing Gatsby's response.
     *
     * @param text Gatsby's response
     * @return a dialog box for Gatsby's message
     */
    public static DialogBox getGatsbyDialog(String text) {
        return new DialogBox(text, "Gatsby", true);
    }
}
