import javafx.scene.paint.Color;

/** Animal Cow, a subclass of Animal. */
class Cow extends Animal {

  /**
   * Constructs a new Cow.
   *
   * @param first the first coordinate of this Cow.
   * @param second the second coordinate of this Cow.
   */
  Cow(int first, int second) {
    super();
    colour = Color.PINK.darker().darker();
    appearance = "MOO";
    this.first = first;
    this.second = second;
  }

  /** Helps animal clear stomach. */
  void clearStomach() {
    System.out.println("Cow stink");
    // Digest.
    AnimalManure newManure = new AnimalManure();
    newManure.manureAppearance = "&";
    newManure.setLocation(first, second);
    Human.myFarmAnimals.add(newManure);
  }
}
