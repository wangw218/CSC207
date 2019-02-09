import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/** A Transit system. */
class TransitSystem {
  /** The total revenue. */
  static double revenue = 0;
  /** The total stops. */
  static int stops = 0;
  /** The daily revenue. */
  static double dailyRevenue = 0;
  /** The daily stops. */
  static int dailyStops = 0;
  /** All users in the system. */
  static Map<String, CardHolder> users;
  /** All cards in the system. */
  static Map<String, Card> cards;
  /** Showing if the system is on or off. */
  static boolean systemOn = false;
  /** The date. */
  static int[] date = new int[3];
  /** The stops in all bus routes. */
  static ArrayList<ArrayList<String>> busStops = new ArrayList<>();
  /** The stations in all subway routes. */
  static ArrayList<ArrayList<String>> subwayStations = new ArrayList<>();

  /** Construct a new transit system. */
  TransitSystem(String stations, String stops) {
    users = new HashMap<>();
    cards = new HashMap<>();
    subwayStations = readStation(stations);
    busStops = readStation(stops);
  }

  /**
   * Read the files to get bus routes and subway stations.
   *
   * @param fileName The file containing routes information.
   * @return All bus and subway routes.
   */
  private static ArrayList<ArrayList<String>> readStation(String fileName) {
    ArrayList<ArrayList<String>> rides = new ArrayList<>();
    try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
      String line = fileReader.readLine(); // Read the first line
      while (line != null) {
        String[] station = line.split(",");
        ArrayList<String> stationList = new ArrayList<>(Arrays.asList(station));
        rides.add(stationList);
        // Read a new line.
        line = fileReader.readLine();
      }
    } catch (IOException e) {
      System.out.println("Station IO Exception!");
    }
    return rides;
  }

  /** Open the system. */
  void openSystem() {
    // record the monthly cost of last month at the first dsy of new month.
    if (date[3] == 1) {
      int[] currentDate = new int[] {date[1], date[2]};
      for (String email : users.keySet()) {
        CardHolder user = users.get(email);
        user.costToMonths.put(currentDate, user.monthlyCost);
        // Reset the monthly cost.
        user.monthlyCost = 0;
      }
    }
    systemOn = true;
  }

  /** Close the system. */
  void closeSystem() {
    System.out.println(dailyReport());
    systemOn = false;
    // Reset the daily revenues and stops.
    dailyRevenue = 0;
    dailyStops = 0;
  }

  /**
   * Generate the daily report.
   *
   * @return the daily report.
   */
  String dailyReport() {
    return "The daily revenue is "
        + dailyRevenue
        + ", and the number of daily stops is "
        + dailyStops
        + "." ;
  }
}
