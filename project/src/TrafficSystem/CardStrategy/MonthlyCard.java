package TrafficSystem.CardStrategy;

import TrafficSystem.Holder.CardHolder;
import TrafficSystem.MainProgram;

/** A monthly card class. */
public class MonthlyCard extends Card {
  /** Valid date for monthly card. */
  private int[] validDate;

  /**
   * Constructs a new MonthlyCard. The price is $100.
   *
   * @param holder Cardholder for the card.
   * @param date Usable date for the card.
   */
  public MonthlyCard(CardHolder holder, int[] date) {
    super(holder, "month");
    addObserver(MainProgram.system);
    addObserver(holder);
    validDate = date;
    double[] price = new double[2];
    price[1] = 100;
    price[0] = 0;
    setChanged();
    notifyObservers(price);
  }

  /**
   * Checks monthly card is valid or not.
   *
   * @param date Valid date.
   * @return Monthly card is valid.
   */
  boolean checkValid(int[] date) {
    boolean valid =
        date[0] == validDate[0]
            && date[1] == validDate[1]
            && date[2] == validDate[2]
            && date[3] == validDate[3];
    return valid && MainProgram.system.systemOn;
  }
}
