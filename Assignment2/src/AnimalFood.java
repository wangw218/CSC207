import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/** Animal Food */
class AnimalFood {

  /** The appearance of the food. */
  private static final String test = "%";
  /** Use for random movement left and right, up and down. */
  double d;
  /** This food's first coordinate. */
  private int x;
  /** This food's second coordinate. */
  private int y;
  /** My colour. Ah, the vagaries of British vs. US spelling. */
  private Color colour;
  /** How this piece of food appears on the screen. */
  private String appearance;

  /** Constructs a new AnimalFood. */
  AnimalFood() {
    colour = Color.GRAY.darker().darker().darker();
    // start off with % as the appearance cause all animal food looks the same
    appearance = AnimalFood.test;
  }

  /**
   * Sets this item's location.
   *
   * @param a the first coordinate.
   * @param b the second coordinate.
   */
  void setLocation(int a, int b) {
    x = a;
    y = b;
  }

  /** Gets the first coordinate. */
  int getX() {
    return x;
  }

  /** Gets the second coordinate. */
  int getY() {
    return y;
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
    drawString(g, appearance, x, y);
  }

  /**
   * Causes this item to take its turn in the farm-pen simulation, blown due to strong winds. Up in
   * this case
   */
  void blownUp() {
    y++;
  }

  /**
   * Causes this item to take its turn in the farm-pen simulation, blown due to strong winds. Down
   * in this case
   */
  void blownDown() {
    y--;
  }

  /**
   * Causes this item to take its turn in the farm-pen simulation, blown due to strong winds. Left
   * in this case
   */
  void blownLeft() {
    x--;
  }

  /**
   * Causes this item to take its turn in the farm-pen simulation, blown due to strong winds. Right
   * in this case
   */
  void blownRight() {
    x++;
  }
}
