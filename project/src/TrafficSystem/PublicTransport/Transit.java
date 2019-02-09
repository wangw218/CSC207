package TrafficSystem.PublicTransport;

import TrafficSystem.MainProgram;
import TrafficSystem.SystemLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/** The Transit. */
public class Transit {
  /** Stations and stops. */
  static ArrayList<ArrayList<String>> lines = new ArrayList<>();

  /**
   * Construct Transit.
   *
   * @param file Path for the file containing stops and stations.
   */
  Transit(String file) {
    lines = readStation(file);
  }

  /**
   * Return all stops and stations.
   *
   * @return An array list containing all stops.
   */
  public static ArrayList<String> getStation() {
    ArrayList<String> allStation = new ArrayList<>();
    for (ArrayList line : lines) {
      for (Object stop : line) {
        if (!allStation.contains(stop)) {
          allStation.add(((String) stop).trim());
        }
      }
    }
    return allStation;
  }

  /**
   * Read and return stops and stations in fileName.
   *
   * @param fileName The path for the file of stops and stations.
   * @return An array list of stops and stations.
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
      e.printStackTrace();
      SystemLogger.getInstance().log("SEVERE", "Unable to read stations.");
    }
    ArrayList<ArrayList<String>> trimed = new ArrayList<>();
    for (int i = 0; i < rides.size(); i++) {
      for (int j = 0; j < rides.get(i).size(); j++) {
        trimed.add(new ArrayList<>());
        trimed.get(i).add(rides.get(i).get(j).trim());
      }
    }
    return trimed;
  }

  /**
   * Get the time that the next subway or bus arrives
   *
   * @return The time that the next subway or bus arrives.
   */
  public static String getNextTime() {
    int hour = MainProgram.system.date[3];
    int minute = MainProgram.system.date[4];
    if (hour == 22 && minute > 45) {
      return "Sorry, the system will close soon";
    } else {
      if (minute == 0) {
        return ("The next will arrive at " + hour + ":" + minute);
      } else if (minute > 0 && minute <= 15) {
        return "The next will arrive at " + hour + ":" + 15;
      } else if (minute > 15 && minute <= 30) {
        return "The next will arrive at " + hour + ":" + 30;
      } else if (minute > 30 && minute <= 45) {
        return "The next will arrive at " + hour + ":" + 45;
      } else {
        int nextHour = hour + 1;
        return "The next will arrive at " + nextHour + ":" + "00";
      }
    }
  }
}
