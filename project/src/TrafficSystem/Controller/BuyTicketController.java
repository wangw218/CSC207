package TrafficSystem.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

/** The scene to buy ticket. */
public class BuyTicketController extends Controller implements Initializable {

  /** Show if the visitor wants to take bus. */
  public static boolean bus;

  @FXML private ToggleGroup group = new ToggleGroup();
  @FXML private RadioButton busSelection;
  @FXML private RadioButton subwaySelection;

  /** Show if the visitor is buying ticket. */
  private boolean buy = VisitorChooseController.buy;

  /**
   * Confirm what ticket the visitor is buying.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void handleConfirm(javafx.event.ActionEvent actionEvent) throws Exception {
    if (group.getSelectedToggle() == busSelection) {
      bus = true;
    } else if (group.getSelectedToggle() == subwaySelection) {
      bus = false;
    }
    if (group.getSelectedToggle() != null) {
      switchScene(actionEvent, "Map.fxml");
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setContentText("Please choose an item: ");
      alert.showAndWait();
    }
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (buy) {
      busSelection.setText("Bus Ticket");
      subwaySelection.setText("Subway Ticket");
    } else {
      busSelection.setText("Bus");
      subwaySelection.setText("Subway");
    }
    group.getToggles().add(busSelection);
    group.getToggles().add(subwaySelection);
    Controller.previousUI = "VisitorChoose.fxml";

    if (UserController.visitor) {
      Controller.previousUI = "VisitorChoose.fxml";
    } else {
      Controller.previousUI = "Cardholder.fxml";
    }
  }
}
