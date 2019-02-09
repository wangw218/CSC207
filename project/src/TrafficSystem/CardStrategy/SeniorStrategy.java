package TrafficSystem.CardStrategy;

import java.io.Serializable;

/** A class deal with senior card strategy. */
public class SeniorStrategy implements FareStrategy, Serializable {
  @Override
  public double calculateSubwayFare(int stop) {
    double FARE = 0.3;
    return stop * FARE;
  }

  @Override
  public double calculateBusFare() {
    return (double) 0;
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
