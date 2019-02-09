package TrafficSystem.PublicTransport;

import TrafficSystem.CardStrategy.FareStrategy;

import java.io.Serializable;
import java.util.ArrayList;

/** A class to track trips. */
public class Trip extends java.util.Observable implements Serializable {
  /** Array list of rides. */
  public ArrayList<Ride> rides;
  /** The cost of the trip. */
  private double cost;

  /** Constructs a new Trip. */
  public Trip() {
    rides = new ArrayList<>();
    cost = 0;
  }

  /**
   * Returns cardholder is on a same trip.
   *
   * @param startTime Start time of the trip.
   * @param startLocation Start location of the trip.
   * @return It is the same trip.
   */
  public boolean sameTrip(int[] startTime, String startLocation) {
    Ride startRide = rides.get(0);
    Ride lastRide = rides.get(rides.size() - 1);
    int[] activeTime = startRide.start;
    if (lastRide.endLocation.equals(startLocation)) {
      return (startTime[0] == activeTime[0]
              && startTime[1] == activeTime[1]
              && startTime[2] == activeTime[2]
              && (startTime[3] - activeTime[3]) < 2
          || (startTime[3] - activeTime[3] == 2 && startTime[4] < activeTime[4]));
    }
    return false;
  }

  /**
   * Ends a normal ride.
   *
   * @param bus The trip is on bus.
   * @param fs The strategy of the card.
   * @param time Time of the trip.
   * @param location End location.
   * @return A double containing num of stops of the trip and the total cost.
   */
  public double[] endTrip(boolean bus, FareStrategy fs, int[] time, String location) {
    double init = cost;
    Ride lastRide = rides.remove(rides.size() - 1);
    int stop;
    if (!bus) {
      stop = Subway.getStop(lastRide.startLocation, location);
      double should_charge = fs.calculateSubwayFare(stop);
      if (init + should_charge > fs.max()) {
        if (fs.max() - init > 0) should_charge = fs.max() - init;
        else {
          should_charge = 0;
        }
      }
      cost += should_charge;
    } else {
      stop = Bus.getStop(lastRide.startLocation, location);
    }
    lastRide.tapOff(time, location);
    rides.add(lastRide);
    double[] out = new double[2];
    out[0] = stop;
    if (cost - init > 0) {
      out[1] = cost - init;
    }
    out[1] = cost - init;
    return out;
  }

  /**
   * Ends the trip without tapping off.
   *
   * @param bus The trip is on bus.
   * @param fs The strategy of the card.
   * @return The total cost.
   */
  public double endTrip(boolean bus, FareStrategy fs) {
    double init = cost;
    double should_charge = 0;
    Ride ride = rides.get(rides.size() - 1);
    if (bus) {
      should_charge += (double) fs.extraBus();
    } else {
      should_charge += (double) fs.extraSubway();
    }

    cost += should_charge;
    ride.tapOff(ride.start, ride.startLocation);
    rides.add(ride);
    if (cost - init < 0) {
      return 0;
    }
    return cost - init;
  }

  /**
   * Adds a new ride.
   *
   * @param ride Current ride.
   * @param fs The strategy of the card.
   * @param missTapIn User failed to tap in.
   * @return The total cost.
   */
  public double addRide(Ride ride, FareStrategy fs, boolean missTapIn) {
    double init = cost;
    double should_charge = 0;
    if (ride.bus) {
      if (!missTapIn) {
        should_charge += fs.calculateBusFare();
      } else {
        should_charge += fs.extraBus();
      }
    } else {
      if (missTapIn) {
        should_charge += fs.extraSubway();
      }
    }
    if ((init + should_charge > fs.max()) && !missTapIn) {
      should_charge = fs.max() - init;
    }
    cost += should_charge;
    rides.add(ride);
    if (cost - init < 0) {
      return 0;
    }
    return cost - init;
  }
  /**
   * Convert the object to String
   *
   * @return the string version of the object.
   */
  @Override
  public String toString() {
    String result;
    Ride startRide = rides.get(0);
    Ride endRide = rides.get(rides.size() - 1);
    int[] start = startRide.start;
    int[] end = endRide.end;
    String startTime =
        start[0] + "-" + start[1] + "-" + start[2] + ", " + start[3] + ":" + start[4];
    String endTime = end[0] + "-" + end[1] + "-" + end[2] + ", " + end[3] + ":" + end[4];
    result = "Enter at " + startRide.startLocation + " at " + startTime + "";
    if (!endTime.equals("0.0.0.0.0")) {
      result += ". Exit at " + endRide.endLocation + " at " + endTime;
    } else {
      result += "and hasn't finish trip";
    }
    return result;
  }
}
