package TrafficSystem.Controller;

import TrafficSystem.CardStrategy.TicketManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/** Input the ticket number. */
public class VisitorNumberController extends Controller implements Initializable {

  /** The ticket number. */
  static String ticketNum;
  /** The text field to input ticket number. */
  @FXML private TextField number;

  /**
   * Switch scene to Map if ticket is valid.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void handleSubmit(javafx.event.ActionEvent actionEvent) throws Exception {

    if (TicketManager.validTicket(number.getText())) {
      ticketNum = number.getText();
      UserController.visitor = true;
      VisitorChooseController.buy = false;
      switchScene(actionEvent, "Map.fxml");
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setContentText("Ticket Does Not Exist");
      alert.showAndWait();
    }
  }

  /** Reset the text field. */
  @FXML
  public void handleReset() {
    number.setText("");
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    number.setText("");
    Controller.previousUI = "VisitorChoose.fxml";
  }
}
