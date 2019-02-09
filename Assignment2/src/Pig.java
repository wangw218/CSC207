import javafx.scene.paint.Color;

/** Animal Pig, a subclass of Animal. */
public class Pig extends Animal {

  /**
   * Constructs a new Pig.
   *
   * @param first the first coordinate of this Pig.
   * @param second the second coordinate of this Pig.
   */
  Pig(int first, int second) {
    super();
    colour = Color.PINK.darker().darker().darker();
    appearance = ":(8)";
    this.first = first;
    this.second = second;
  }

  /** Helps animal clear stomach */
  void clearStomach() {
    System.out.println("Pig stink");
    // Digest.
    AnimalManure newManure = new AnimalManure();
    newManure.manureAppearance = "*";
    newManure.setLocation(first, second);
    Human.myFarmAnimals.add(newManure);
  }
}
