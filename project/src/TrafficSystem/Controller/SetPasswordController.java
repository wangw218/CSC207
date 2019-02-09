package TrafficSystem.Controller;

import TrafficSystem.MainProgram;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/** The scene to initialize password. */
public class SetPasswordController extends Controller implements Initializable {

  /** The text field to input password. */
  @FXML private TextField passwordField;

  /** Confirm to set the password. */
  @FXML
  private void handleConfirm(javafx.event.ActionEvent actionEvent) throws Exception {
    if (passwordField.getText().equals("")) {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in the blank");
      alert.setTitle("Warning");
      alert.showAndWait();
    } else {
      MainProgram.system.setPassword(passwordField.getText());
      switchScene(actionEvent, "Dashboard.fxml");
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
