package TrafficSystem.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/** The visitor can choose service. */
public class VisitorChooseController extends Controller implements Initializable {

  /** Shows whether the visitor is buying ticket. */
  static boolean buy;

  /**
   * Switch scene to BuyTicket.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  public void buy(javafx.event.ActionEvent actionEvent) throws Exception {
    buy = true;
    switchScene(actionEvent, "BuyTicket.fxml");
  }

  /**
   * Switch scene to VisitorNumber.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  private void take(javafx.event.ActionEvent actionEvent) throws Exception {
    buy = false;
    switchScene(actionEvent, "VisitorNumber.fxml");
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Controller.previousUI = "User.fxml";
  }
}
