package TrafficSystem.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Related to the first scene when the program starts. Also other controller subclasses inherit two
 * methods from this class: back to this scene and switchScene method.
 */
public class Controller implements Initializable {
  /** Previous interface of the UI. */
  static String previousUI;

  /**
   * Helper Method for all controller classes to switch scenes.
   *
   * @param actionEvent the click event
   * @param sceneName which scene you want to switch to: file name end with ".fxml"
   * @throws Exception wrong name entered
   */
  void switchScene(javafx.event.ActionEvent actionEvent, String sceneName) throws Exception {
    Parent root = FXMLLoader.load(Controller.class.getResource(sceneName));
    Scene newScene = new Scene(root, 400, 400);
    Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    window.setScene(newScene);
    window.show();
  }

  /**
   * Switch scene to the previous user interface.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  public void back(javafx.event.ActionEvent actionEvent) throws Exception {
    switchScene(actionEvent, previousUI);
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {}
}
