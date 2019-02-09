import javafx.scene.paint.Color;

/** Animal Cat, a subclass of Animal. */
class Cat extends Animal {

  /**
   * Constructs a new Cat.
   *
   * @param first the first coordinate of this Cat.
   * @param second the second coordinate of this Cat.
   */
  Cat(int first, int second) {
    super();
    this.first = first;
    this.second = second;
    colour = Color.BLUE.darker().darker();
    appearance = "CAT";
  }

  /** Helps animal clear stomach. */
  void clearStomach() {
    System.out.println("Cat stink");
    // Digest.
    AnimalManure newManure = new AnimalManure();
    newManure.manureAppearance = "#";
    newManure.setLocation(first, second);
    Human.myFarmAnimals.add(newManure);
  }
}
