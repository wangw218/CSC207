package TrafficSystem.Controller;

import TrafficSystem.CardStrategy.Card;
import TrafficSystem.CardStrategy.CardManager;
import TrafficSystem.CardStrategy.MonthlyCard;
import TrafficSystem.Holder.CardHolder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/** The scene for cardholder. */
public class CardholderController extends TrafficSystem.Controller.Controller implements Initializable {

  /** The card number. */
  static String cardNumber;
  /** The card holder. */
  static CardHolder cardHolder;
  /** The cards of the user. */
  @FXML private ChoiceBox<String> cards;
  /** The available load money amount */
  @FXML private ChoiceBox moneyAmount;
  /** Buy special card. */
  @FXML private Button identity;
  /** The button to load load money. */
  @FXML private Button load;
  /** The button to activate card. */
  @FXML private Button act;
  /** The button to remove card. */
  @FXML private Button remove;
  /** The button to suspend card */
  @FXML private Button sus;
  /** The button to travel */
  @FXML private Button travel;
  /** The GridPane. */
  @FXML private GridPane gp;
  /** The button to check balance. */
  @FXML private Button checkBalance;

  /**
   * Changes the user's name.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  public void handleChangeName(javafx.event.ActionEvent actionEvent) throws Exception {
    NewNameController.user = cardHolder;
    switchScene(actionEvent, "NewName.fxml");
  }

  /** Views three recent trips. */
  @FXML
  private void handleViewTrip() {
    String trip = cardHolder.viewTrip();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Recent trips");
    alert.setContentText(trip);
    alert.showAndWait();
  }

  /** Views monthly average cost. */
  @FXML
  private void handleTrackAverage() {
    String average = cardHolder.trackAverage();
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Average cost");
    alert.setContentText(average);
    alert.showAndWait();
  }

  /** Removes the certain card. */
  @FXML
  private void handleRemoveCard() {
    if (cardNumber != null) {
      String result = cardNumber;
      cards.getItems().remove(cardNumber);
      cardHolder.removeCard(result);
      cards.setDisable(false);
      Alert alert =
          new Alert(
              Alert.AlertType.CONFIRMATION, "The card " + result + " is removed from your cards.");
      alert.setTitle("Remove Card");
      alert.showAndWait();
      remove.setDisable(true);
      act.setDisable(true);
      sus.setDisable(true);
      travel.setDisable(true);
      load.setDisable(true);
      checkBalance.setDisable(true);
      moneyAmount.setDisable(true);
    }
  }

  /** Suspends the certain card. */
  @FXML
  private void handleSuspendCard() {
    Card card = CardManager.cards.get(cardNumber);
    card.suspendCard();
    Alert alert =
        new Alert(Alert.AlertType.CONFIRMATION, "The card " + cardNumber + " is suspended.");
    alert.setTitle("Suspend Card");
    alert.showAndWait();
    sus.setDisable(true);
    travel.setDisable(true);
    act.setDisable(false);
  }

  /** Activates the certain card. */
  @FXML
  private void handleActivateCard() {
    Card card = CardManager.cards.get(cardNumber);
    card.activateCard();
    Alert alert =
        new Alert(Alert.AlertType.CONFIRMATION, "The card " + cardNumber + " is activated.");
    alert.setTitle("Activate Card");
    alert.showAndWait();
    act.setDisable(true);
    travel.setDisable(false);
    sus.setDisable(false);
  }

  /** Buy monthly card. */
  @FXML
  public void handleBuyMonthlyCard() {
    Card card = cardHolder.addMonthlyCard();
    cards.getItems().add(card.number);
    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "Cost: $100. The monthly card's number is "
                + card.number
                + "，and the card balance is $"
                + card.balance);
    alert.setTitle("Monthly Card");
    alert.showAndWait();
  }

  /** Buy normal card. */
  @FXML
  public void handleBuyNormalCard() {
    Card card = cardHolder.addCard("normal");
    cards.getItems().add(card.number);
    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "The new card's number is " + card.number + ", and the balance is $" + card.balance);
    alert.setTitle("Normal Card");
    alert.showAndWait();
  }

  /** Buy special card. */
  @FXML
  public void handleBuySSCard() {
    Card card = cardHolder.addCard(cardHolder.identity);
    cards.getItems().add(card.number);
    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "The new card's number is " + card.number + ", and the balance is $" + card.balance);
    alert.setTitle("Student / Senior");
    alert.showAndWait();
  }

  /** Load money to the card. */
  @FXML
  private void handleLoadMoney() {
    String money = moneyAmount.getValue().toString();
    StringBuilder newM = new StringBuilder();
    for (int i = 1; i < money.length(); i++) {
      newM.append(money.charAt(i));
    }
    CardManager.cards.get(cardNumber).loadMoney(Integer.parseInt(newM.toString()));
    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "Successfully loaded money.The current balance is $"
                + CardManager.cards.get(cardNumber).balance);
    alert.setTitle("Load Money");
    alert.showAndWait();
  }

  /**
   * Travel.
   *
   * @param actionEvent the click event
   * @throws Exception wrong name entered
   */
  @FXML
  public void handleTravel(javafx.event.ActionEvent actionEvent) throws Exception {
    UserController.visitor = false;
    switchScene(actionEvent, "BuyTicket.fxml");
  }

  /** Check the balance */
  @FXML
  public void handleBalance() {
    Alert alert =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            cardNumber + " balance is $" + CardManager.cards.get(cardNumber).balance);
    alert.setTitle("Balance");
    alert.showAndWait();
  }

  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    moneyAmount.setDisable(true);
    if (cardHolder.identity.equals("Other")) {
      gp.getChildren().remove(identity);
    } else {
      identity.setText("Buy " + cardHolder.identity + " Card");
    }
    if (cardHolder.cards.size() != 0) {
      cards.getItems().addAll(cardHolder.cards);
    }
    sus.setDisable(true);
    act.setDisable(true);
    remove.setDisable(true);
    travel.setDisable(true);
    load.setDisable(true);
    Controller.previousUI = "User.fxml";
  }

  /** Choose the card. */
  @FXML
  public void handleChooseCard() {
    cardNumber = cards.getValue();
    moneyAmount.setDisable(false);
    sus.setDisable(false);
    remove.setDisable(false);
    travel.setDisable(false);

    checkBalance.setDisable(false);
    if ((CardManager.cards.get(cardNumber)) instanceof MonthlyCard) {
      load.setDisable(true);
    } else {
      load.setDisable(false);
    }
  }
}
