package TrafficSystem.CardStrategy;

/** Interface of card strategies. */
public interface FareStrategy {
  /**
   * Returns the total cost of subway.
   *
   * @param stop Number of stops that has passed.
   * @return Total cost of the trip in subway.
   */
  double calculateSubwayFare(int stop);

  /**
   * Returns the total cost of bus.
   *
   * @return Total cost of the trip in bus.
   */
  double calculateBusFare();

  /**
   * Returns bus extra cost.
   *
   * @return Extra cost for bus.
   */
  int extraBus();

  /**
   * Returns subway extra cost.
   *
   * @return Extra cost for subway.
   */
  int extraSubway();

  /**
   * Return the maximum cost of the trip.
   *
   * @return The maximum cost.
   */
  int max();
}
