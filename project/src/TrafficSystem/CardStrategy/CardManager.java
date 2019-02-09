package TrafficSystem.CardStrategy;

import TrafficSystem.MainProgram;
import TrafficSystem.Serialization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** A class stores all Card. */
public class CardManager {
  /** Contains all cards. */
  public static Map<String, Card> cards = new HashMap<>();
  /** Total card number in CardManager. */
  static int totalCards = 0;

  /**
   * Adds a Card into cards.
   *
   * @param cardNumber Card number of the card.
   * @param card Card.
   */
  public static void addCard(String cardNumber, Card card) {
    cards.put(cardNumber, card);
    totalCards += 1;
  }

  /**
   * Adds all Card back into CardManager.
   *
   * @throws IOException Failed to read the file.
   * @throws ClassNotFoundException Failed to find the class.
   */
  public void addAllCards() throws IOException, ClassNotFoundException {
    String path = "phase2/Files/Cards/";
    File file = new File(path);
    File[] fs = file.listFiles();
    assert fs != null;
    for (File f : fs) {
      Card card = null;
      if (!f.isDirectory()) card = (Card) Serialization.deserialize(f.getAbsolutePath());
      assert card != null;
      cards.put(card.number, card);
      card.addObserver(MainProgram.system);
      card.addObserver(card.holder);
      totalCards += 1;
    }
  }
}
