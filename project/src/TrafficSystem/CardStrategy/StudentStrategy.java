package TrafficSystem.CardStrategy;

import java.io.Serializable;

/** A class deal with student card strategy. */
public class StudentStrategy implements FareStrategy, Serializable {
  @Override
  public double calculateSubwayFare(int stop) {
    double FARE = 0.4;
    return stop * FARE;
  }

  @Override
  public double calculateBusFare() {
    return 1.5;
  }

  @Override
  public int extraBus() {
    return 5;
  }

  @Override
  public int extraSubway() {
    return 10;
  }

  @Override
  public int max() {
    return 6;
  }
}
