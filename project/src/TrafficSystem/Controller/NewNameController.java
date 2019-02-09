package TrafficSystem.Controller;

import TrafficSystem.Holder.CardHolder;
import TrafficSystem.SystemLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewNameController extends Controller implements Initializable {
  /** The user. */
  public static CardHolder user;
  /** The new name entered. */
  @FXML private TextField newName;

  /**
   * Switch to CardHolder.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void handleConfirm(javafx.event.ActionEvent actionEvent) throws Exception {
    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "Do you want to change name to " + newName.getText() + "?",
            ButtonType.YES,
            ButtonType.NO);
    alert.setTitle("Confirmation");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.YES) {
      user.changeName(newName.getText());
      SystemLogger.getInstance()
          .log("INFO", user.getEmail() + " changes name to " + newName.getText());
      switchScene(actionEvent, "Cardholder.fxml");
    }
  }

  /** Reset the text field. */
  @FXML
  public void reset() {
    newName.setText("");
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Controller.previousUI = "Cardholder.fxml";
  }
}
