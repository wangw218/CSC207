package TrafficSystem.PublicTransport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bus extends Transit {
  /** Connection of the stops. */
  private static Map<String, ArrayList<String>> connection = new HashMap<>();

  /**
   * Constructs a new Bus.
   *
   * @param stops All bus stops.
   */
  public Bus(String stops) {
    super(stops);
    get_connection();
  }

  /**
   * Gets how many stops has passed.
   *
   * @param start Where the line starts.
   * @param end Where the line ends.
   * @return Number of stops that has passed.
   */
  public static int getStop(String start, String end) {
    for (ArrayList<String> line : lines) {
      if (line.contains(start)) {
        return Math.abs(line.indexOf(start) - line.indexOf(end));
      }
    }
    return 0;
  }

  /**
   * Return whether start and end is on the same bus line.
   *
   * @param start Start stop.
   * @param end End stop.
   * @return start and end is on the same line.
   */
  public static boolean same_line(String start, String end) {
    return start.equals(end) || connection.get(start).contains(end);
  }

  /** Gets connection between old stop and new stop. */
  private void get_connection() {
    for (ArrayList<String> line : Transit.lines) {
      for (String stop : line) {
        ArrayList<String> old;
        if (connection.containsKey(stop)) {
          old = connection.get(stop);
        } else {
          old = new ArrayList<>();
        }
        for (String otherStop : line) {
          if (!otherStop.equals(stop)) {
            old.add(otherStop);
          }
        }
        connection.put(stop, old);
      }
    }
  }
}
