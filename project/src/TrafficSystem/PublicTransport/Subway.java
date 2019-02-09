package TrafficSystem.PublicTransport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Subway extends Transit {
  /** Connection of the stops. */
  private static Map<String, ArrayList<String>> connections = new HashMap<>();

  /**
   * Construct a new Subway.
   *
   * @param stations Subway stations.
   */
  public Subway(String stations) {
    super(stations);
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
    ArrayList<ArrayList> discovered = new ArrayList<>();
    ArrayList<ArrayList> queue = new ArrayList<>();
    queue.add(buildList(start, 0));
    discovered.add(buildList(start, 0));

    while (!queue.isEmpty()) {
      ArrayList curr = queue.remove(0);
      int current_dis = (Integer) curr.get(1) + 1;
      int si = Subway.connections.get(curr.get(0)).size();
      for (int i = 0; i < si; i++) {
        String new_stop = Subway.connections.get(curr.get(0)).get(i);
        if (firstTime(discovered, new_stop.trim())) {
          queue.add(buildList(new_stop, current_dis));
          discovered.add(buildList(new_stop, current_dis));
        }
      }
    }

    for (ArrayList check : discovered) {
      if (check.get(0).equals(end)) {
        return (Integer) check.get(1);
      }
    }
    return 0;
  }

  /**
   * A helper method combining start and num.
   *
   * @param start Where the line starts.
   * @param num Num of stations passes start.
   * @return Combination of start and num.
   */
  private static ArrayList buildList(String start, int num) {
    ArrayList combine = new ArrayList();
    combine.add(start);
    combine.add(num);
    return combine;
  }

  /**
   * Check the distance of stop in discovered.
   *
   * @param discovered List of stations.
   * @param stop Station needs to be checked.
   * @return The distance has been checked.
   */
  private static boolean firstTime(ArrayList<ArrayList> discovered, String stop) {
    for (ArrayList aDiscovered : discovered) {
      String dis = (String) aDiscovered.get(0);
      if (dis.equals(stop)) {
        return false;
      }
    }
    return true;
  }

  /** Gets connection between old stop and new stop. */
  private void get_connection() {
    for (ArrayList<String> line : Transit.lines) {
      for (int j = 0; j < line.size() - 1; j++) {
        String key = line.get(j);
        ArrayList<String> old;
        if (connections.containsKey(key)) {
          old = connections.get(key);
        } else {
          old = new ArrayList<>();
        }
        old.add(line.get(j + 1));
        connections.put(key, old);
      }
      for (int j = line.size() - 1; j > 0; j--) {
        String key = line.get(j);
        ArrayList<String> old;
        if (connections.containsKey(key)) {
          old = connections.get(key);
        } else {
          old = new ArrayList<>();
        }
        old.add(line.get(j - 1));
        connections.put(key, old);
      }
    }
  }
}
