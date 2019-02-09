package TrafficSystem.Controller;

import TrafficSystem.Holder.CardHolderManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/** The scene for current user. */
public class CurUserController extends Controller implements Initializable {

  /** The text field to input email. */
  @FXML private TextField emails;

  /**
   * Log in to the account.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void handleCurUser(javafx.event.ActionEvent actionEvent) throws Exception {
    String email = emails.getText();
    if (CardHolderManager.users.containsKey(email)) {
      CardholderController.cardHolder = CardHolderManager.users.get(email);
      switchScene(actionEvent, "Cardholder.fxml");
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Wrong email address");
      alert.setContentText("User does not exist, please enter a valid email.");
      alert.showAndWait();
    }
  }

  /**
   * Switch scene to new holder.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void handleSignUp(javafx.event.ActionEvent actionEvent) throws Exception {
    switchScene(actionEvent, "NewHolder.fxml");
  }

  /** Reset the text field. */
  @FXML
  public void Reset() {
    emails.setText("");
  }

  /** Initialize the scene. */
  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    emails.setText("");
    Controller.previousUI = "User.fxml";
  }
}
