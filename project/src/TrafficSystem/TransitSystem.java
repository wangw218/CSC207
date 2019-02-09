package TrafficSystem;

import TrafficSystem.Holder.CardHolder;
import TrafficSystem.Holder.CardHolderManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/** A TrafficSystem.PublicTransport.Transit system. */
public class TransitSystem implements Serializable, java.util.Observer {
  /** Showing if the system is on or off. */
  public boolean systemOn = false;
  /** The date. */
  public int[] date = {2011, 1, 1, 5, 0};

  /** Time is changed. */
  public boolean changed = false;
  /** The daily revenue. */
  private double dailyRevenue;
  /** The daily stops. */
  private int dailyStops;
  /** The monthly revenue. */
  private double monthlyRevenue;
  /** The daily stops. */
  private int monthlyStops;
  /** The password. */
  private String password = "";
  /** The times each station is visited. */
  private Map<String, Integer> stationVisited = new HashMap<>();
  /** Daily report for the transit system. */
  private ArrayList<String> dailyReports = new ArrayList<>();

  /** Construct a new transit system. */
  TransitSystem() {
    serialize();
  }

  /**
   * Return whether the password exist.
   *
   * @return True if the password exit, else false.
   */
  boolean passwordExists() {
    return (!password.equals(""));
  }

  public void setPassword(String newPassword) {
    password = newPassword;
    serialize();
  }

  public boolean passwordEquals(String pswd) {
    return password.equals(pswd);
  }

  private void serialize() {
    Serialization.serialize("phase2/Files/TrafficSystem.TransitSystem.ser", this);
  }

  /** Open the system. */
  public void openSystem() {
    // record the monthly cost of last month at the first day of new month.
    if (date[2] == 1) {
      monthlyRevenue = 0;
      monthlyStops = 0;
      for (String email : CardHolderManager.users.keySet()) {
        CardHolder user = CardHolderManager.users.get(email);
        // Reset the monthly cost.
        user.monthlyCost = 0;
      }
    }
    systemOn = true;
    serialize();
  }

  /** Close the system. */
  public void closeSystem() throws IOException {
    systemOn = false;
    // Reset the daily revenues and stops.
    if (dailyReports.size() > 29) {
      dailyReports.remove(0);
    }
    int[] yearMonthDay = {date[0], date[1], date[2]};
    String dailyReport =
        Arrays.toString(yearMonthDay)
            + ": The daily revenue is "
            + dailyRevenue
            + ", and the number of daily stops is "
            + dailyStops
            + ".";
    FileWriter fw = new FileWriter("phase2/Files/DailyReports.txt", true);
    BufferedWriter bufw = new BufferedWriter(fw);
    bufw.write(dailyReport);
    bufw.newLine();
    bufw.flush();
    bufw.close();
    dailyReports.add(dailyReport);
    dailyRevenue = 0;
    dailyStops = 0;
    setDate(getTomorrow());
    serialize();
  }

  /**
   * Generate the daily report.
   *
   * @return the daily report.
   */
  public String dailyReport() {
    StringBuilder result = new StringBuilder();
    for (String dailyReport : dailyReports) {
      result.append(dailyReport).append("\n");
    }
    return result.toString();
  }

  /**
   * Generate the monthly report.
   *
   * @return the monthly report.
   */
  public String monthlyReport() {
    return "The monthly revenue is "
        + monthlyRevenue
        + ", and the number of total stops is "
        + monthlyStops
        + ".";
  }

  public void setDate(int[] newDate) {
    date = newDate;
    serialize();
  }

  public void updateStationVisited(String station) {
    if (stationVisited.containsKey(station)) {
      stationVisited.put(station, stationVisited.get(station) + 1);
    } else {
      stationVisited.put(station, 1);
    }
    serialize();
  }

  public StringBuilder viewStationVisited() {
    StringBuilder result = new StringBuilder();
    for (String station : stationVisited.keySet()) {
      result.append(station).append(": ").append(stationVisited.get(station)).append("\n");
    }
    return result;
  }

  private int[] getTomorrow() {
    if (date[1] == 12) {
      if (date[2] == 30) {
        return new int[] {date[0] + 1, 1, 1, 5, 0};
      } else {
        return new int[] {date[0], 12, date[2] + 1, 5, 0};
      }
    } else {
      if (date[2] == 30) {
        return new int[] {date[0], date[1] + 1, 1, 5, 0};
      } else {
        return new int[] {date[0], date[1], date[2] + 1, 5, 0};
      }
    }
  }

  @Override
  public void update(java.util.Observable o, Object arg) {
    if (arg instanceof double[]) {
      monthlyStops += (int) ((double[]) arg)[0];
      dailyStops += (int) ((double[]) arg)[0];
      monthlyRevenue += ((double[]) arg)[1];
      dailyRevenue += ((double[]) arg)[1];
    }
    serialize();
  }
}
