package TrafficSystem.Holder;

import TrafficSystem.CardStrategy.Card;
import TrafficSystem.CardStrategy.CardManager;
import TrafficSystem.CardStrategy.MonthlyCard;
import TrafficSystem.MainProgram;
import TrafficSystem.PublicTransport.Trip;
import TrafficSystem.Serialization;
import TrafficSystem.SystemLogger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** A card holder. */
public class CardHolder implements Serializable, java.util.Observer {
  /** The monthly cost of the card holder. */
  public double monthlyCost = 0;
  /** The numbers of holder's cards. */
  public ArrayList<String> cards;
  /** The holder's identity. */
  public String identity;
  /** All the trips of the card holder. */
  private ArrayList<Trip> trips;
  /** Every month's cost of the card holder. */
  private Map<String, Double> costToMonths;
  /** The card holder's name. */
  private String name;
  /** The card holder's email. */
  private String email;

  /** Construct a new card holder. */
  public CardHolder(String email, String name, String identity) {
    this.email = email;
    this.name = name;
    cards = new ArrayList<>();
    trips = new ArrayList<>();
    costToMonths = new HashMap<>();
    this.identity = identity;
    CardHolderManager.addHolder(email, this);
    serialize();
  }

  /**
   * Change the card holder's name.
   *
   * @param newName the new name of the card holder.
   */
  public void changeName(String newName) {
    name = newName;
    serialize();
  }

  /** Print recent trips */
  public String viewTrip() {
    if (trips.size() == 0) {
      return "There is no recent trip available.";
    } else if (trips.size() < 4) {
      // Print all rides if the number of total trips is less than 4.
      StringBuilder result = new StringBuilder();
      for (Trip trip : trips) {
        result.append(trip.toString()).append("\n");
      }
      return result.toString();
    }
    return (trips.get(trips.size() - 3)
        + "\n"
        + trips.get(trips.size() - 2)
        + "\n"
        + trips.get(trips.size() - 1));
  }

  /** Add a new card. */
  public Card addCard(String id) {
    Card card = new Card(this, id);
    cards.add(card.number);
    CardManager.addCard(card.number, card);
    serialize();
    SystemLogger.getInstance().log("INFO", name + " " + email + " buys a new card: " + card.number);
    return card;
  }

  /**
   * Adds a new monthly card.
   *
   * @return A new Card.
   */
  public Card addMonthlyCard() {
    MonthlyCard card = new MonthlyCard(this, MainProgram.system.date);
    cards.add(card.number);
    CardManager.addCard(card.number, card);
    serialize();
    SystemLogger.getInstance()
        .log("INFO", name + " " + email + " buys a new monthly card: " + card.number);
    return card;
  }

  /**
   * Remove a certain card.
   *
   * @param cardNumber the number of the card to be removed
   */
  public void removeCard(String cardNumber) {
    for (String number : cards) {
      if (number.equals(cardNumber)) {
        cards.remove(number);
        SystemLogger.getInstance()
            .log("INFO", "Card " + number + " has been removed from " + name + "'s account.");
        break;
      }
    }
    serialize();
  }

  /** Print the average cost per month. */
  public String trackAverage() {
    double totalCost = 0;
    int totalMonths = 0;
    for (String month : costToMonths.keySet()) {
      totalMonths += 1;
      totalCost += costToMonths.get(month);
    }
    if (totalMonths == 0) {
      return "The average cost is $0.0 per month.";
    } else {
      return "The average cost is $" + totalCost / totalMonths + " per month";
    }
  }

  /**
   * Add money to monthly card.
   *
   * @param cost Monthly cost.
   */
  private void addCostToMonth(double cost) {
    int[] date = MainProgram.system.date;
    String month = "";
    month += date[0];
    month += "-" + date[1];
    if (costToMonths.containsKey(month)) {
      double totalCost = costToMonths.get(month) + cost;
      costToMonths.put(month, totalCost);
    } else {
      costToMonths.put(month, cost);
    }
  }

  /**
   * Get Cardholder email.
   *
   * @return email for cardholder.
   */
  public String getEmail() {
    return this.email;
  }

  /** Serialize Cardholder. */
  private void serialize() {
    String directory = "phase2/Files/CardHolders/" + email + ".ser";
    Serialization.serialize(directory, this);
  }

  /**
   * Update
   *
   * @param o the observable object
   * @param arg the argument passed by observable object
   */
  @Override
  public void update(java.util.Observable o, Object arg) {
    if (arg instanceof double[]) {
      monthlyCost += ((double[]) arg)[1];
      addCostToMonth(((double[]) arg)[1]);
    } else if (arg instanceof Trip) {
      trips.add((Trip) arg);
    }
    serialize();
  }
}
