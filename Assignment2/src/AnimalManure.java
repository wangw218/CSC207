import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/** Animal Manure */
public class AnimalManure {

  /** The appearance of the feces. */
  String manureAppearance = ".";
  /** This feces's first coordinate. */
  private int x;
  /** This feces's second coordinate. */
  private int y;
  /** My colour. Ah, the vagaries of British vs. US spelling. */
  private Color colour;
  /** How this piece of feces appears on the screen. */
  private String appearance;
  /** The start time of this AnimalManure. */
  private long startTime;

  /** Constructs a new feces. */
  AnimalManure() {
    colour = Color.BLACK.darker().darker().darker();
    appearance = manureAppearance;
    startTime = System.currentTimeMillis();
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

  /** Sets the start time of this AnimalManure. */
  long getStartTime() {
    return startTime;
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
}
