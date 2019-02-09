package TrafficSystem.Controller;

import TrafficSystem.CardStrategy.CardReader;
import TrafficSystem.CardStrategy.Ticket;
import TrafficSystem.MainProgram;
import TrafficSystem.PublicTransport.Bus;
import TrafficSystem.PublicTransport.Subway;
import TrafficSystem.PublicTransport.Transit;
import TrafficSystem.SystemLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MapController extends Controller implements Initializable {

  /** The start location. */
  private static String start = "";
  /** The end location. */
  private static String end = "";
  /** Choose to start. */
  @FXML private ChoiceBox<String> startBox;
  /** Choose to end. */
  @FXML private ChoiceBox<String> endBox;
  /** Choose to tap in. */
  @FXML private Button in;
  /** Choose to tap off. */
  @FXML private Button off;
  /** Anchor Pane */
  @FXML private AnchorPane ap;
  /** The label of endT */
  @FXML private Label endT;
  /** The label of startT */
  @FXML private Label startT;
  /** The date. */
  private int[] date;
  /** Whether the user is a visitor. */
  private boolean visitor = UserController.visitor;
  /** Whether the visitor is buying. */
  private boolean buy = VisitorChooseController.buy;

  /** Tap in at one location. */
  @FXML
  public void handleTapIn() {
    date = MainProgram.system.date;
    if (visitor && buy) {
      start = startBox.getValue();
      end = endBox.getValue();
      startBox.setDisable(true);
      endBox.setDisable(true);
    } else if (visitor) {
      String Tnumber = VisitorNumberController.ticketNum;
      start = startBox.getValue();
      String msg = CardReader.tapIn(Tnumber, BuyTicketController.bus, start);
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Information");
      alert.setContentText(msg);
      alert.showAndWait();
    } else {
      start = startBox.getValue();
      String msg =
          CardReader.tapIn(CardholderController.cardNumber, BuyTicketController.bus, date, start);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Information");
      alert.setContentText(msg);
      alert.showAndWait();
    }
  }

  /** Tap off at one location and the button to buy ticket. */
  @FXML
  public void handleTapOff() {
    date = MainProgram.system.date;
    if (visitor && buy) {
      start = startBox.getValue();
      end = endBox.getValue();
      if (start.equals(end)) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText("Start and end locations can't be the same!");
        alert.showAndWait();
      } else {
        if (BuyTicketController.bus && Bus.same_line(start, end)) {
          Alert alert = new Alert(Alert.AlertType.WARNING);
          alert.setTitle("Warning");
          alert.setContentText("Start and end locations are not on the same line!");
          alert.showAndWait();
        } else {
          /* The ticket. */
          Ticket ticket = new Ticket(start, end, BuyTicketController.bus);
          double cost;
          if (BuyTicketController.bus) {
            cost = 2;
          } else {
            cost = 0.5 * Subway.getStop(start, end);
          }
          SystemLogger.getInstance()
              .log("INFO", "Visitor buys a ticket " + ticket.number + ": " + start + "-" + end);
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("Success");
          alert.setContentText(
              "Visitor buys a ticket "
                  + ticket.number
                  + ": "
                  + start
                  + "-"
                  + end
                  + ". Cost $"
                  + cost);
          alert.showAndWait();
        }
      }
    } else if (visitor) {
      String ticketNum = VisitorNumberController.ticketNum;
      end = startBox.getValue();
      String check = CardReader.tapOff(ticketNum, BuyTicketController.bus, end);
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Information");
      alert.setContentText(check);
      alert.showAndWait();
    } else {
      end = startBox.getValue();
      String msg =
          CardReader.tapOff(CardholderController.cardNumber, BuyTicketController.bus, date, end);
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Information");
      alert.setContentText(msg);
      alert.showAndWait();
    }
  }

  /**
   * Switch scene to SetTime.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  public void handleSetTime(javafx.event.ActionEvent actionEvent) throws Exception {
    switchScene(actionEvent, "SetTime.fxml");
    Controller.previousUI = "Map.fxml";
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ArrayList<String> line;
    if (BuyTicketController.bus) {
      Bus bus = new Bus("phase2/stops.txt");
      line = bus.getStation();
    } else {
      Subway subway = new Subway("phase2/stations.txt");
      line = subway.getStation();
    }
    if (visitor && buy) {
      ap.getChildren().remove(in);
      off.setText("Buy");
      startBox.getItems().addAll(line);
      endBox.getItems().addAll(line);
      startBox.setValue(line.get(0));
      endBox.setValue(line.get(0));
    } else {
      startBox.getItems().addAll(line);
      ap.getChildren().remove(endBox);
      ap.getChildren().remove(endT);
      startT.setText("Location");
      startBox.setValue(line.get(0));
    }

    Controller.previousUI = "BuyTicket.fxml";
  }

  /** Show when the next bus or subway arrives. */
  @FXML
  private void handleNextTime() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setContentText(Transit.getNextTime());
    alert.showAndWait();
  }
}
