/**
 * Wind used to blow Animal Food. If the wind was last blowing up then it is likely to keep blowing
 * up. Same for left/right.
 */
class Wind {

  /** Which way was the wind last blowing up or down? */
  private static int lastUp = 0;

  /** Which way was the wind last blowing left or right? */
  private static int lastLeft = 0;

  /**
   * Helper function of windBlowingUp and windBlowingLeft.
   *
   * @return the direction the wind is blowing
   */
  private static int windBlowing(int upOrLeft) {
    /*
     *upOrLeft was not zero. Keep blowing the same direction 30% the time.
     *Turn around 10% of the time. Otherwise no wind.
     */
    double d = Math.random();
    if (upOrLeft != 0) {
      if (d <= 0.3) {
        return upOrLeft;
      } else if (d >= 0.9) {
        upOrLeft = -upOrLeft;
      } else upOrLeft = 0;
    } else {
      // upOrLeft was zero. Change wind 10% each direction.
      if (Math.random() < 0.1) {
        upOrLeft = -1;
      } else if (Math.random() < 0.1) {
        upOrLeft = 1;
      }
    }
    return upOrLeft;
  }

  /**
   * Gets the vertical direction this Wind is blowing. -1 if the wind is blowing up 1 if the wind is
   * blowing down 0 if the wind isn't blowing.
   *
   * @return the vertical direction this wind is blowing
   */
  static int windBlowingUp() {
    /*
     * If LastUp was not zero, keep blowing the same direction 30% the time and turn around 10% of the time.
     *Otherwise no wind.
     *If LastUp was zero, change wind 10% updown each.
     */
    lastUp = windBlowing(lastUp);
    return lastUp;
  }

  /**
   * Gets the horizontal direction this Wind is blowing. -1 if the wind is blowing left 1 if the
   * wind is blowing right 0 if the wind isn't blowing
   *
   * @return the horizontal direction this wind is blowing
   */
  static int windBlowingLeft() {
    /*
     *If LastLeft was not zero, keep blowing the same direction 30% the time and turn around 10% of the time.
     *Otherwise no wind.
     *If LastLeft was zero, change wind 10% left or right each.
     */
    lastLeft = windBlowing(lastLeft);
    return lastLeft;
  }
}
