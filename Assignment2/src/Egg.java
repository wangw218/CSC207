import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/** An egg that a farmer collects. */
class Egg {
  /** This food's first coordinate. */
  private int x;
  /** This food's second coordinate. */
  private int y;
  /** How this piece of food appears on the screen. */
  private static final char eggShape = 'o';

  /**
   * Set this item's location.
   *
   * @param a the horizontal coordinate of this Egg.
   * @param b the vertical coordinate of this Egg.
   */
  void setLocation(int a, int b) {
    x = a;
    y = b;
  }

  /**
   * Draws this farm pen item.
   *
   * @param g the graphics context in which to draw this item.
   */
  void draw(GraphicsContext g) {
    System.out.println("Brown stuff");
    g.setFill(Color.ROSYBROWN);
    g.fillText(String.valueOf(eggShape), x * Main.CHAR_WIDTH, y * Main.CHAR_HEIGHT);
  }

  /** Gets the first coordinate of this Egg */
  int getX() {
    return x;
  }

  /** Gets the second coordinate of this Egg. */
  int getY() {
    return y;
  }
}
