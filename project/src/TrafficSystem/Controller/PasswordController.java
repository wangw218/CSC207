package TrafficSystem.Controller;

import TrafficSystem.MainProgram;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/** Input the password. */
public class PasswordController extends Controller implements Initializable {

  /** The text field to input password. */
  @FXML private TextField passwordField;

  /**
   * Check if the password is true.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void handleSubmit(javafx.event.ActionEvent actionEvent) throws Exception {
    if (MainProgram.system.passwordEquals(passwordField.getText())) {
      switchScene(actionEvent, "Admin.fxml");
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setContentText("Wrong password. Please try again");
      alert.showAndWait();
    }
  }

  /** Reset the text field. */
  @FXML
  public void handleReset() {
    passwordField.setText("");
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    passwordField.setText("");
    Controller.previousUI = "Dashboard.fxml";
  }
}
