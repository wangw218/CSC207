import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/** A Human */
class Human {
  // The max index of the row of this Human can reach.
  private static final int row = 480 / Main.CHAR_WIDTH - 1;
  // The max index of the column of this Human can reach.
  private static final int column = 640 / Main.CHAR_HEIGHT - 1;
  // Creates a ArrayList myFarmAnimal.
  static ArrayList<Object> myFarmAnimals = new ArrayList<>();
  /** How this lovely human appears on the screen. */
  private String appearance;
  /** Indicates whether this human is moving right. */
  private boolean goingRight;
  /** Indicates whether this human is moving up. */
  private boolean goingUp;
  /** This human's first coordinate. */
  private int first;
  /** This human's second coordinate. */
  private int second;
  // The egg this Human is going to pick up.
  private Egg target = null;
  // This Human's basket which is used to contain eggs.
  private ArrayList<Egg> myBasket = new ArrayList<>();
  // The graphics context in which to draw the string.
  private GraphicsContext g;

  /**
   * Constructs a new Human.
   *
   * @param first the first coordinate of this Human
   * @param second the second coordinate of this Human.
   */
  Human(int first, int second) {
    appearance = "human";
    goingRight = true;
    goingUp = true;
    this.first = first;
    this.second = second;
  }

  /** Causes this human to drop down 4 pieces of food all around. */
  private void dropFood() {
    AnimalFood food = new AnimalFood();
    if (first > 0 && second > 0) {
      food.setLocation(first - 1, second - 1);
      myFarmAnimals.add(food);
    }
    if (first < row && second > 0) {
      food = new AnimalFood();
      food.setLocation(first + 1, second - 1);
      myFarmAnimals.add(food);
    }
    if (first > 0 && second < column) {
      food = new AnimalFood();
      food.setLocation(first - 1, second + 1);
      myFarmAnimals.add(food);
    }
    if (first < row && second < column) {
      food = new AnimalFood();
      food.setLocation(first + 1, second + 1);
      myFarmAnimals.add(food);
    }
  }

  /** Gets all of the current food. */
  static ArrayList<AnimalFood> foodHere() {
    ArrayList<AnimalFood> arr = new ArrayList<>();
    for (Object o : Human.myFarmAnimals) {
      if (o instanceof AnimalFood) arr.add((AnimalFood) o);
    }
    return arr;
  }

  /** Builds and initializes this human's forward and backward appearances. */
  private String reverseAppearance() {
    StringBuilder reverse = new StringBuilder();
    for (int i = appearance.length() - 1; i >= 0; i--) {
      switch (appearance.charAt(i)) {
        case ')':
          reverse.append('(');
          break;
        case '(':
          reverse.append(')');
          break;
        case '>':
          reverse.append('<');
          break;
        case '<':
          reverse.append('>');
          break;
        case '}':
          reverse.append('{');
          break;
        case '{':
          reverse.append('}');
          break;
        case '[':
          reverse.append(']');
          break;
        case ']':
          reverse.append('[');
          break;
        default:
          reverse.append(appearance.charAt(i));
          break;
      }
    }

    return reverse.toString();
  }

  /** Turns this human around, causing it to reverse direction. */
  private void turnAround() {
    goingRight = !goingRight;
    appearance = reverseAppearance();
  }

  /** Turns this human around vertically, causing it to reverse direction. */
  private void turnAroundVertically() {
    goingUp = !goingUp;
    appearance = reverseAppearance();
  }

  /**
   * Draws the given string in the given graphics context at at the given cursor location.
   *
   * @param g the graphics context in which to draw the string.
   * @param s the string to draw.
   * @param x the x-coordinate of the string's cursor location.
   * @param y the y-coordinate of the string's cursor location.
   */
  private void drawString(GraphicsContext g, String s, int x, int y) {
    g.fillText(s, x * Main.CHAR_WIDTH, y * Main.CHAR_HEIGHT);
    g.fillText("Eggs: " + myBasket.size(), 2 * Main.CHAR_WIDTH, 2 * Main.CHAR_HEIGHT);
  }

  /**
   * Draws this farm pen item.
   *
   * @param g the graphics context in which to draw this item.
   */
  void draw(GraphicsContext g) {
    this.g = g;
    g.setFill(Color.SANDYBROWN.darker());
    drawString(g, appearance, first, second);
  }

  /** Causes this item to take its turn in the farm-pen simulation. */
  void move() {
    // No egg to pick.
    if (target == null) {
      // All the current eggs.
      ArrayList<Egg> arr = Chicken.eggsHere();
      if (arr.size() == 0) target = null;
      else if (arr.size() == 1) target = arr.get(0);
      else { // Get the index of the egg with the minimum distance with Human.
        int min = 0;
        System.out.println(arr.size());
        for (int i = 0; i != arr.size() - 1; i++) {
          Egg egg1 = arr.get(min);
          Egg egg2 = arr.get(i + 1);
          double distance1 =
              Math.sqrt(Math.pow(first - egg1.getX(), 2) + Math.pow(second - egg1.getY(), 2));
          double distance2 =
              Math.sqrt(Math.pow(first - egg2.getX(), 2) + Math.pow(second - egg2.getY(), 2));
          if (distance2 < distance1) {
            min = i + 1;
          }
        }
        // Sets the nearest egg to target.
        target = arr.get(min);
      }
    }
    // Has eggs to pick.
    if (target != null) {
      System.out.println(
          "Target acquired: "
              + target.getX()
              + " "
              + target.getY()
              + "| Me: "
              + first
              + " "
              + second);
      // Am I on an egg?
      if (first == target.getX() && second == target.getY()) {
        System.out.println("Egg!");
        this.myBasket.add(target);
        // the index the target in Human.myFarmAnimals
        int index = Human.myFarmAnimals.indexOf(target);
        // remove the target on the canvas
        target = null;
        Human.myFarmAnimals.set(index, null);
        // Are the eggs in my basket be the times of 12?
        if (myBasket.size() % 12 == 0) {
          System.out.println("Dozen!");
          g.fillText("Eggs: " + myBasket.size(), Main.CHAR_WIDTH, Main.CHAR_HEIGHT);
        }
      } else {
        // move towards the egg
        if (first < target.getX()) {
          if (!goingRight) turnAround(); // If originally going left, turn around to the right.
          if (first < row) first += 1; // move one spot to the right
        } else if (first > target.getX()) {
          if (goingRight) turnAround(); // If originally going right, turn around to the left.
          if (first > 0) first -= 1; // move one spot to the left.
        }
        if (second < target.getY()) {
          if (!goingUp)
            turnAroundVertically(); // If originally going down, turn around vertically upward.
          if (second < column) second += 1; // move one spot upward

        } else if (second > target.getY()) {
          if (goingUp)
            turnAroundVertically(); // If originally going up, turn around vertically downward.
          if (second > 0) second -= 1; // move one spot downward
        }
      }
    } else { // no egg to pick up
      // Move one spot to the right or left, up or down.
      double d = Math.random();
      if (d < 0.1) {
        // Move one spot to the right.
        if (first < row) first++;
        // Fit the direction the Human is going to and turn around when getting to fence.
        if (!goingRight || first == row) turnAround();
        // Move one spot up.
        if (second < column) second++;
        // Fit the direction the Human is going to and turn around vertically when getting to the
        // fence.
        if (!goingUp || second == column) turnAroundVertically();
      } else if (d < 0.2) {
        // Move one spot to the left.
        if (first > 0) first--;
        // Fit the direction the Human is going to and turn around when getting to the fence.
        if (first == 0 || goingRight) turnAround();
        // Move one spot down.
        if (second > 0) second--;
        // Fit the direction the Human is going to and turn around vertically when getting to the
        // fence.
        if (second == 0 || goingUp) turnAroundVertically();
      }
    }

    // Figure out whether I should drop food.
    double d = Math.random();
    if (d < 0.05) {
      dropFood();
    }
    // Figure out whether I turn around.
    if (d < 0.1) {
      turnAround();
    }
  }
}
