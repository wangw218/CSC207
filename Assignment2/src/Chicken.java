import javafx.scene.paint.Color;

import java.util.ArrayList;

/** Animal Chicken, a subclass of Animal. */
public class Chicken extends Animal {

  /**
   * Constructs a new Chicken.
   *
   * @param first the first coordinate of this Chicken.
   * @param second the second coordinate of this Chicken.
   */
  Chicken(int first, int second) {
    super();
    this.first = first;
    this.second = second;
    colour = Color.RED;
    appearance = "/'/>";
  }

  /** Finds all current eggs. */
  static ArrayList<Egg> eggsHere() {
    ArrayList<Egg> arr = new ArrayList<>();
    for (Object o : Human.myFarmAnimals) {
      if (o instanceof Egg) arr.add((Egg) o);
    }
    return arr;
  }

  /** Causes this item to take its turn in the farm-pen simulation. */
  void move() {
    super.move();
    // Every now and then lay an egg.
    double d2 = Math.random();
    if (d2 < 0.1) {
      layEgg();
    }
  }

  /** Lays an egg. */
  private void layEgg() {
    System.out.println("Breakfast! " + "Egg loc: " + first + " " + second);
    int hereX = first;
    int hereY = second;
    Egg egg = new Egg();
    egg.setLocation(hereX, hereY);
    Human.myFarmAnimals.add(egg);
  }

  /** Helps animal clear stomach. */
  void clearStomach() {
    System.out.println("New stuff to make things grow.");
    // Digest.
    AnimalManure newManure = new AnimalManure();
    newManure.manureAppearance = ".";
    newManure.setLocation(first, second);
    Human.myFarmAnimals.add(newManure);
  }
}
