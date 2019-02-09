import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** A card holder. */
class CardHolder {
  /** The card holder's name. */
  private String name;
  /** The card holder's email. */
  private String email;
  /** The monthly cost of the card holder. */
  int monthlyCost = 0;
  /** The cards of the card holder. */
  private ArrayList<Card> cards;
  /** All the rides of the card holder. */
  ArrayList<Ride> rides;
  /** Every month's cost of the card holder. */
  Map<int[], Integer> costToMonths;

  /** Construct a new card holder. */
  CardHolder(String email, String name) {
    this.email = email;
    this.name = name;
    cards = new ArrayList<>();
    rides = new ArrayList<>();
    costToMonths = new HashMap<>();
  }

  /**
   * Change the card holder's name.
   *
   * @param newName the new name of the card holder.
   */
  void changeName(String newName) {
    name = newName;
    System.out.println("Your username has changed to " + name + ".");
  }

  /** Print recent trips */
  void viewTrip() {
    // Print all rides if the number of total trips is less than 4.
    if (rides.size() < 4) {
      for (Ride ride : rides) {
        System.out.println(ride);
      }
    } else {
      System.out.println(rides.get(rides.size() - 3));
      System.out.println(rides.get(rides.size() - 2));
      System.out.println(rides.get(rides.size() - 1));
    }
  }

  /** Add a new card. */
  void addCard() {
    Card card = new Card(this);
    TransitSystem.cards.put(card.number, card);
    cards.add(card);
    System.out.println(name + " " + email + " buys a new card.");
  }

  /**
   * Remove a certain card.
   *
   * @param cardNumber the number of the card to be removed
   */
  void removeCard(String cardNumber) {
    for (Card card : cards) {
      if (card.number.equals(cardNumber)) {
        cards.remove(card);
        System.out.println(
            "Card " + card.number + " has been removed from " + name + "'s account.");
      }
    }
  }

  /**
   * Suspend a certain card.
   *
   * @param cardNumber the number of the card to be suspended.
   */
  void suspendCard(String cardNumber) {
    for (Card card : cards) {
      if (card.number.equals(cardNumber)) {
        card.suspendCard();
      }
    }
  }

  /**
   * Activate a certain card.
   *
   * @param cardNumber the number of the card to be activated.
   */
  void activateCard(String cardNumber) {
    for (Card card : cards) {
      if (card.number.equals(cardNumber)) {
        card.activateCard();
      }
    }
  }

  /** Print the average cost per month. */
  void trackAverage() {
    int totalCost = 0;
    int totalMonths = 0;
    for (int[] month : costToMonths.keySet()) {
      totalMonths += 1;
      totalCost += costToMonths.get(month);
    }
    if (totalMonths == 0) {
      System.out.println("The average cost is 0.0.");
    } else {
      System.out.println("The average cost is"+ totalCost / totalMonths);
    }
  }
}
