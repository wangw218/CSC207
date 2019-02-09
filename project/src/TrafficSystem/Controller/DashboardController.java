package TrafficSystem.Controller;

import TrafficSystem.MainProgram;
import TrafficSystem.SystemLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

/** The dashboard. */
public class DashboardController extends Controller implements Initializable {
  @FXML TextField time;

  /**
   * Switch scene to user.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  public void handleUser(javafx.event.ActionEvent actionEvent) throws Exception {
    if (MainProgram.system.systemOn) {
      switchScene(actionEvent, "User.fxml");
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setContentText("The system is currently closed. Please try again later.");
      alert.showAndWait();
      SystemLogger.getInstance().log("FINE", "System is closed. Failed to open user interface.");
    }
  }

  /**
   * Switch scene to Password or setPassword.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void handleAdmin(javafx.event.ActionEvent actionEvent) throws Exception {
    if (MainProgram.passwordSet) {
      switchScene(actionEvent, "Password.fxml");
    } else {
      switchScene(actionEvent, "SetPassword.fxml");
      MainProgram.passwordSet = true;
    }
  }

  /**
   * Set the time.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void handleSetTime(javafx.event.ActionEvent actionEvent) throws Exception {
    switchScene(actionEvent, "SetTime.fxml");
    Controller.previousUI = "Dashboard.fxml";
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    DecimalFormat df = new DecimalFormat("00");
    time.setText(
        String.valueOf(df.format(MainProgram.system.date[0]))
            + "--"
            + String.valueOf(df.format(MainProgram.system.date[1]))
            + "--"
            + String.valueOf(df.format(MainProgram.system.date[2]))
            + "  "
            + String.valueOf(df.format(MainProgram.system.date[3]))
            + ":"
            + String.valueOf(df.format(MainProgram.system.date[4])));
  }
}
