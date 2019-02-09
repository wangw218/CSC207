package TrafficSystem.CardStrategy;

import java.io.Serializable;

/** A class deal with monthly card strategy. */
public class MonthlyStrategy implements FareStrategy, Serializable {
  @Override
  public double calculateSubwayFare(int stop) {
    return 0;
  }

  @Override
  public double calculateBusFare() {
    return 0;
  }

  @Override
  public int extraBus() {
    return 0;
  }

  @Override
  public int extraSubway() {
    return 0;
  }

  @Override
  public int max() {
    return 0;
  }
}
