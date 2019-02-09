package TrafficSystem.Controller;

import TrafficSystem.MainProgram;
import TrafficSystem.SystemLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** The scene for admin. */
public class AdminController extends Controller implements Initializable {

  @FXML private Button open;
  @FXML private Button close;

  @FXML private TextArea report;
  @FXML private TextArea stationView;

  /** Show how many times each stop is visited. */
  @FXML
  private void handleVisited() {
    StringBuilder stations = MainProgram.system.viewStationVisited();
    stationView.setText(stations.toString());
  }

  /** Open the system. */
  @FXML
  private void handleOpen() {
    MainProgram.system.openSystem();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The system has been opened.");
    alert.setTitle("Confirmation");
    alert.showAndWait();
    open.setDisable(true);
    MainProgram.system.systemOn = true;
    close.setDisable(false);
    SystemLogger.getInstance().log("FINE", "Opens the system.");
  }

  /** Close the system. */
  @FXML
  private void handleClose() throws IOException {
    MainProgram.system.closeSystem();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "The system has been closed.");
    alert.setTitle("Confirmation");
    alert.showAndWait();
    close.setDisable(true);
    MainProgram.system.systemOn = false;
    open.setDisable(false);
    SystemLogger.getInstance().log("FINE", "Closes the system.");
  }

  /** Show daily report. */
  @FXML
  private void handlePrint() {
    report.setText(MainProgram.system.dailyReport());
  }

  /** Print monthly report. */
  @FXML
  public void handlePrintMonthly() {
    report.setText(MainProgram.system.monthlyReport());
  }

  /** Initialize the admin interface. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (MainProgram.system.systemOn) {
      open.setDisable(true);
      close.setDisable(false);
    } else {
      close.setDisable(true);
      open.setDisable(false);
    }
    Controller.previousUI = "Dashboard.fxml";
  }
}
