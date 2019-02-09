import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/** Abstract class Animal */
public abstract class Animal {
  /** How this animal appears on the screen. */
  String appearance;
  /** Indicates whether this animal moving right. */
  private boolean goingRight;
  /** Indicates whether this Chicken is moving up. */
  private boolean goingUp;
  /** This animal's first coordinate. */
  int first;
  /** The colour of this animal. */
  Color colour;
  /** This animal's second coordinate. */
  int second;
  /** The max index of the row of this animal can reach. */
  private static final int row = Main.CAN_WIDTH / Main.CHAR_WIDTH - 1;
  /** The max index of the column this animal can reach. */
  private static final int column = Main.CAN_HEIGHT / Main.CHAR_HEIGHT - 1;

  /** Constructs a new Animal. */
  Animal() {
    goingRight = true;
    goingUp = true;
  }

  /** Builds and initializes this Animal's forward and backward appearances. */
  private String reverseAppearance() {
    StringBuilder reverse = new StringBuilder();
    for (int i = appearance.length() - 1; i >= 0; i--) {
      switch (appearance.charAt(i)) {
        case '\\':
          reverse.append('/');
          break;
        case '/':
          reverse.append('\\');
          break;
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

  /** Turns this animal around, causing it to reverse direction. */
  private void turnAround() {
    goingRight = !goingRight;
    appearance = reverseAppearance();
  }

  /** Turns this animal around vertically, causing it to reverse direction. */
  private void turnAroundVertical() {
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
    g.setFill(colour);
    g.fillText(s, x * Main.CHAR_WIDTH, y * Main.CHAR_HEIGHT);
  }

  /**
   * Draws this farm pen item.
   *
   * @param g the graphics context in which to draw this item.
   */
  void draw(GraphicsContext g) {
    drawString(g, appearance, first, second);
  }

  /** Causes this item to take its turn in the farm-pen simulation. */
  void move() {
    // Move one spot to the right or left.
    if (goingRight) {
      if (first < row) first += 1;
    } else if (first > 0) {
      first -= 1;
    }
    // If this Animal get to the horizontal fence, turn around horizontally.
    if (first == 0 || first == row) turnAround();

    // Move one spot to the up or down.
    if (goingUp) {
      if (second < column) second += 1;
    } else if (second > 0) second -= 1;
    // If this Animal get to the vertical fence, turn around vertically.
    if (second == 0 || second == column) turnAroundVertical();

    // When this animal meets AnimalFood, eat it.
    ArrayList<AnimalFood> arr = Human.foodHere();
    for (int i = 0; i != arr.size(); i++) {
      AnimalFood food = arr.get(i);
      int x = food.getX();
      int y = food.getY();
      if (first == x && second == y) {
        int index1 = arr.indexOf(food);
        arr.set(index1, null);
        int index2 = Human.myFarmAnimals.indexOf(food);
        Human.myFarmAnimals.set(index2, null);
      }
    }

    // Sometimes food doesn't sit well in the stomach, so this animal have to clear its stomach.
    double d1 = Math.random();
    if (d1 < 0.2) {
      clearStomach();
    }

    // Sometimes this animal is hungry, needing to eat.
    double d2 = Math.random();
    // The animal needs to turn around horizontally.
    if (d2 < 0.1) {
      turnAround();
    }
    // Need to turn around vertically.
    d2 = Math.random();
    if (d2 > 0.8) {
      turnAroundVertical();
    }
  }

  /** Helps animal clear stomach. */
  abstract void clearStomach();
}
