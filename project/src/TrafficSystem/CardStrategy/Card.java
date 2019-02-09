package TrafficSystem.CardStrategy;

import TrafficSystem.Holder.CardHolder;
import TrafficSystem.MainProgram;
import TrafficSystem.PublicTransport.Trip;
import TrafficSystem.Serialization;
import TrafficSystem.SystemLogger;

import java.io.Serializable;
import java.util.ArrayList;

/** A class stores information and set changes to cards. */
public class Card extends java.util.Observable implements Serializable {
  /** Card number. */
  public String number;
  /** The original balance of a new card. */
  public double balance = 19;
  /** The cardholder for the card. */
  public CardHolder holder;
  /** Card is on bus. */
  boolean onBus = false;
  /** Card is on subway. */
  boolean onSubway = false;
  /** History trip for the card. */
  ArrayList<Trip> history;
  /** The strategy for the card. */
  FareStrategy fs;
  /** The card is active. */
  private boolean active;

  /**
   * Constructs a new Card.
   *
   * @param holder Cardholder for the card.
   * @param type Type for the card, month/student/senior card.
   */
  public Card(CardHolder holder, String type) {
    int digit = (int) (Math.log10(CardManager.totalCards + 1) + 1);
    number = repeat(8 - digit) + CardManager.totalCards;
    history = new ArrayList<>();
    this.holder = holder;
    active = true;
    switch (type) {
      case "Student":
        fs = new StudentStrategy();
        break;
      case "Senior":
        fs = new SeniorStrategy();
        break;
      case "month":
        fs = new MonthlyStrategy();
        balance = 0;
        break;
      default:
        fs = new NormalStrategy();
        break;
    }
    addObserver(this.holder);
    addObserver(MainProgram.system);
    serialize();
  }

  /**
   * Changes the balance of the card.
   *
   * @param change the number that needs to be changed.
   */
  void changeBalance(double change) {
    change = change * 100;
    change = Math.round(change);
    change = (change / 100);
    balance -= change;
    balance = balance * 100;
    balance = Math.round(balance);
    balance = (balance / 100);
    double[] out = new double[2];
    out[1] = change;
    setChanged();
    notifyObservers(out);
    serialize();
  }

  /**
   * Check if the card is valid or not.
   *
   * @return Card is valid.
   */
  boolean checkValid() {
    return this.active && MainProgram.system.systemOn;
  }

  /**
   * Changes the stop.
   *
   * @param stop New stop.
   */
  void changeStop(int stop) {
    if (stop != 0) {
      setChanged();
    }
    double[] out = new double[2];
    out[0] = stop;
    notifyObservers(out);
    serialize();
  }

  /**
   * Adds a new trip.
   *
   * @param newTrip New trip of the card.
   */
  void addTrip(Trip newTrip) {
    setChanged();
    notifyObservers(newTrip);
    serialize();
  }

  /** Modifies the trip. */
  void modifyTrip() {
    serialize();
    setChanged();
    notifyObservers(null);
  }

  /**
   * A helper method to add 0s into card number.
   *
   * @param times Repeating times.
   * @return Repeating 0s of card number.
   */
  private String repeat(int times) {
    return new String(new char[times]).replace("\0", "0");
  }

  /** Suspends the card. */
  public void suspendCard() {
    this.active = false;
    serialize();
    SystemLogger.getInstance().log("INFO", "Card " + number + " is suspended.");
  }

  /** Serializes Card. */
  private void serialize() {
    Serialization.serialize("phase2/Files/Cards/" + number + ".ser", this);
  }

  /** Activates Card. */
  public void activateCard() {
    this.active = true;
    serialize();
    SystemLogger.getInstance().log("INFO", "Card " + number + " is activated.");
  }

  /**
   * load money to the card
   *
   * @param amount the amount that load to the card balance
   */
  public void loadMoney(int amount) {
    if (checkValid()) {
      balance += amount;
      SystemLogger.getInstance()
          .log(
              "INFO",
              "Adds " + amount + " dollars to card " + number + ". Card balance: " + balance);
    }
    serialize();
  }
}
