package TrafficSystem.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController extends Controller implements Initializable {

  /** Shows the whether the user is a visitor. */
  static boolean visitor;

  /**
   * Switch scene to CurUser.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  public void handleCardholder(javafx.event.ActionEvent actionEvent) throws Exception {
    switchScene(actionEvent, "CurUser.fxml");
  }

  /**
   * Switch scene to VistorChoose.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  public void handleVisitor(javafx.event.ActionEvent actionEvent) throws Exception {
    visitor = true;
    switchScene(actionEvent, "VisitorChoose.fxml");
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Controller.previousUI = "Dashboard.fxml";
  }
}
