package TrafficSystem.PublicTransport;

import java.io.Serializable;

public class Ride implements Serializable {
  /** Start location. */
  public String startLocation;
  /** Start time. */
  int[] start;
  /** End location. */
  String endLocation = "";
  /** A bus ride. */
  boolean bus;
  /** End time. */
  int[] end = new int[5];

  private boolean notTap = false;

  /**
   * Construct a new Ride.
   *
   * @param startTime Start time of the ride.
   * @param startLocation Start location of the ride.
   * @param bus The ride is on bus.
   */
  public Ride(int[] startTime, String startLocation, boolean bus) {
    this.start = startTime;
    this.startLocation = startLocation;
    this.bus = bus;
  }

  /**
   * Construct a new Ride.
   *
   * @param startTime Start time of the ride.
   * @param startLocation Start location of the ride.
   * @param bus The ride is on bus.
   * @param notTap Failed to tap in.
   */
  public Ride(int[] startTime, String startLocation, boolean bus, boolean notTap) {
    this.start = startTime;
    this.startLocation = startLocation;
    this.bus = bus;
    this.notTap = notTap;
  }

  /**
   * User taps off.
   *
   * @param endTime Tap off time.
   * @param endLocation Tap off location.
   */
  public void tapOff(int[] endTime, String endLocation) {
    end = endTime;
    this.endLocation = endLocation;
  }

  /**
   * Convert the object to String
   *
   * @return the string version of the object.
   */
  @Override
  public String toString() {
    String startTime = start[0] + "." + start[1] + "." + start[2] + "." + start[3] + "." + start[4];
    String endTime = end[0] + "." + end[1] + "." + end[2] + "." + end[3] + "." + end[4];
    String string = "enter at " + startLocation + " at " + startTime + ".";
    if (!endTime.equals("0.0.0.0.0")) {
      string += ". exit at " + endLocation + " at " + endTime;
    }
    return string;
  }
}
