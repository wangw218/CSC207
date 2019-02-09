package TrafficSystem.Controller;

import TrafficSystem.Holder.CardHolder;
import TrafficSystem.Holder.CardHolderManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** The scene for constructing new holder. */
public class NewHolderController extends Controller implements Initializable {
  /** The identity for new holder. */
  static String id;
  /** The email for new holder. */
  private static String email;
  /** The identity for new holder. */
  @FXML private ChoiceBox<String> identity;
  /** The email for new holder. */
  @FXML private TextField NewEmail;
  /** The name for new holder. */
  @FXML private TextField NewUserName;
  /** The name for new holder. */
  private String name;
  /**
   * The regex to match email.
   *
   * <p>This was adapted from a post in stack overflow forum here:
   * https://stackoverflow.com/questions/8204680/java-regex-email
   */
  private Pattern regex = Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");

  /** Reset the text field. */
  @FXML
  public void Reset() {
    NewEmail.setText("");
    NewUserName.setText("");
    identity.getSelectionModel().selectFirst();
  }

  /** Initialize the scene. */
  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    identity.getItems().addAll("Student", "Senior", "Other");
    identity.setValue("Other");
    NewEmail.setText("");
    NewUserName.setText("");
    Controller.previousUI = "CurUser.fxml";
  }

  /** Construct a new user. */
  @FXML
  private void handleNewUser(javafx.event.ActionEvent actionEvent) throws Exception {
    getInfo();
    Matcher matcher = regex.matcher(email);
    boolean valid = matcher.matches();

    // When the user is not filling in all blanks.
    if (email.equals("") || name.equals("")) {
      Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all blanks.");
      alert.setTitle("Warning");
      alert.showAndWait();

      // When the email entered is not valid.
    } else if (!valid) {
      Alert alert =
          new Alert(Alert.AlertType.WARNING, "The email is not valid. Please try another one!");
      alert.setTitle("Warning");
      alert.showAndWait();

      // When the email is already used.
    } else if (CardHolderManager.validEmail(email)) {
      Alert alert =
          new Alert(Alert.AlertType.WARNING, "This email is used.Please try another one!");
      alert.setTitle("Warning");
      alert.showAndWait();

      // When the email is valid.
    } else {
      CardholderController.cardHolder = new CardHolder(email, name, id);
      switchScene(actionEvent, "Cardholder.fxml");
    }
  }

  /** Get info from the text field. */
  @FXML
  private void getInfo() {
    email = NewEmail.getText();
    name = NewUserName.getText();
    id = identity.getValue();
  }
}
