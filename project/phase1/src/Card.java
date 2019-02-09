import java.util.ArrayList;
import java.util.Arrays;

class Card {
  private static int totalCard = 0;
  private final int CHARGE = 10;
  private final int MAXIMUM = 6;
  private final int BUS_COST = 2;
  String number;
  private double balance = 19;
  private boolean onBus = false;
  private boolean onSubway = false;
  private int[] activeTime;
  private ArrayList<Ride> history;
  private double cost = 0;
  private CardHolder holder;
  private boolean active;

  Card(CardHolder holder) {
    totalCard += 1;
    int digit = (int) (Math.log10(totalCard) + 1);
    number = repeat(8 - digit) + totalCard;
    history = new ArrayList<>();
    activeTime = new int[5];
    this.holder = holder;
    active = true;
  }

  private String repeat(int times) {
    return new String(new char[times]).replace("\0", "0");
  }

  void suspendCard() {
    this.active = false;
  }

  void activateCard() {
    this.active = true;
  }

  private void endTrip(
      boolean inActive, int[] endTime, String endLocation, boolean miss, boolean busTrip) {
    Ride lastRide = history.remove(history.size() - 1);
    holder.rides.remove(lastRide);
    if (busTrip) {
      lastRide.tapOff(endTime, endLocation);
      // find the bus line for the end location
      int index = 0;
      for (int i = 0; i < TransitSystem.busStops.size(); i++) {
        boolean in = false;
        ArrayList<String> thisBus = TransitSystem.busStops.get(i);
        for (String thisBu : thisBus) {
          if (thisBu.contains(endLocation)) {
            in = true;
          }
          if (in) {
            break;
          }
        }
        if (in) {
          index = i;
          break;
        }
      }
      // find the number of stops
      ArrayList<String> bus = TransitSystem.busStops.get(index);
      String startLocation = lastRide.startLocation;
      int startIndex = 0;
      int endIndex = 0;
      for (int i = 0; i < bus.size(); i++) {
        if (bus.get(i).contains(startLocation)) {
          startIndex = i;
        } else if (bus.get(i).contains(endLocation)) {
          endIndex = i;
        }
      }
      TransitSystem.stops += Math.abs((endIndex - startIndex)) + 1;
      TransitSystem.dailyStops += Math.abs((endIndex - startIndex)) + 1;
    } else {
      // if ending a subway ride
      if (miss) {
        lastRide.tapOff(endTime, (TransitSystem.subwayStations.get(0)).get(0));
        balance = balance - CHARGE;
        TransitSystem.revenue += CHARGE;
        TransitSystem.dailyRevenue += CHARGE;
        lastRide.cost += CHARGE;
      } else {
        //        lastRide.tapOff(endTime, endLocation);
        int startIndex = 0;
        int endIndex = 0;
        ArrayList<String> sub = TransitSystem.subwayStations.get(0);
        for (int i = 0; i < TransitSystem.subwayStations.get(0).size(); i++) {
          if (sub.get(i).contains(lastRide.startLocation)) {
            startIndex = i;
          } else if (sub.get(i).contains(lastRide.endLocation)) {
            endIndex = i;
          }
          if (startIndex != 0 && endIndex != 0) {
            break;
          }
        }
        double stopCost = 0.5 * (Math.abs((endIndex - startIndex)));
        TransitSystem.stops += Math.abs((endIndex - startIndex)) + 1;
        TransitSystem.dailyStops += Math.abs((endIndex - startIndex)) + 1;
        boolean inThreeHour = false;
        // see if in three hour
        int[] start = lastRide.start;
        int[] end = lastRide.end;
        if (start[0] == end[0]
                && start[1] == end[1]
                && start[2] == end[2]
                && (end[3] - start[3]) < 3
            || (end[3] - start[3] == 3 && end[4] < start[4])) {
          inThreeHour = true;
        }
        if (stopCost > MAXIMUM && inThreeHour) {
          stopCost = MAXIMUM;
        }
        if (!inActive) {
          balance = balance - stopCost;
          lastRide.cost += stopCost;
          cost = stopCost;
          TransitSystem.revenue += stopCost;
          TransitSystem.dailyRevenue += stopCost;
        } else {
          if (cost < MAXIMUM) {
            double add = cost + stopCost;
            add = Math.min(add, MAXIMUM);
            balance = balance - (add - cost);
            TransitSystem.revenue += (add - cost);
            TransitSystem.dailyRevenue += (add - cost);
            lastRide.cost = add - cost;
            cost = add;
          }
        }
      }
    }
    history.add(lastRide);
  }

  private void startTrip(boolean bus, boolean inActive, String startLocation, int[] start) {
    Ride newRide = new Ride(start, startLocation);
    if (bus) {
      if (!inActive) {
        activeTime = start;
        balance = balance - BUS_COST;
        TransitSystem.revenue += BUS_COST;
        TransitSystem.dailyRevenue += BUS_COST;
        newRide.cost += BUS_COST;
        cost = cost + BUS_COST;
      }
      // if in active mode
      else {
        if (cost < MAXIMUM) {
          if (cost < MAXIMUM - BUS_COST) {
            balance = balance - BUS_COST;
            TransitSystem.revenue += BUS_COST;
            TransitSystem.dailyRevenue += BUS_COST;
            newRide.cost += BUS_COST;
            cost = cost + BUS_COST;
          } else {
            balance = balance - (MAXIMUM - cost);
            TransitSystem.revenue += (MAXIMUM - cost);
            newRide.cost += (MAXIMUM - cost);
            cost = MAXIMUM;
          }
        }
      }
      history.add(newRide);
      holder.rides.add(newRide);
    } else {
      if (!inActive) {
        activeTime = start;
      }
      history.add(newRide);
      holder.rides.add(newRide);
    }
  }

  void tapIn(boolean bus, int[] start, String startLocation, boolean readEvent) {
    active = active && TransitSystem.systemOn;
    if (active) {
      // check to see if in active time
      boolean inActive = false;
      if (start[0] == activeTime[0]
              && start[1] == activeTime[1]
              && start[2] == activeTime[2]
              && (start[3] - activeTime[3]) < 2
          || (start[3] - activeTime[3] == 2 && start[4] < activeTime[4])) {
        inActive = true;
      } else {
        cost = 0;
      }
      // end unfinished bus or subway trip
      if (onSubway || onBus) {
        Ride lastRide = history.get(history.size() - 1);
        int[] endTime = new int[5];
        System.arraycopy(lastRide.start, 0, endTime, 0, 3);
        endTime[3] = 23;
        endTime[4] = 59;
        endTrip(inActive, endTime, startLocation, true, onBus);
        inActive = false;
        cost = 0;
      }
      // about new trip
      if (balance < 0) {
<<<<<<< HEAD
          if(readEvent){
        System.out.println("Sorry your balance is too low, please load money before taking trip");}
=======
        if (Main.write) {
          System.out.println("Sorry your balance is too low, please load money before taking trip");
        }
>>>>>>> aa3926ad7ca912f0cdc107f2fe0165243913e9b9
      } else {
        // check if is the same trip
        if (!inActive || !startLocation.contains(history.get(history.size() - 1).endLocation)) {
          inActive = false;
          cost = 0;
        }
        startTrip(bus, inActive, startLocation, start);
<<<<<<< HEAD
        if (readEvent) {
=======
        if (Main.write) {
>>>>>>> aa3926ad7ca912f0cdc107f2fe0165243913e9b9
          System.out.println(
              "Card "
                  + this.number
                  + " taps in at "
                  + startLocation
                  + " at "
                  + Arrays.toString(start)
                  + ". The card balance is "
                  + balance
                  + ".");
        }
      }
      if (bus) {
        onBus = true;
        onSubway = false;
      } else {
        onBus = false;
        onSubway = true;
      }
    } else {
<<<<<<< HEAD
      if (readEvent) {
=======
      if (Main.write) {
>>>>>>> aa3926ad7ca912f0cdc107f2fe0165243913e9b9
        System.out.println("Sorry the system is currently closed");
      }
    }
  }

  /**
   * @param end the time that cardholder taped off t
   * @param endLocation location where the cardholder tapped off
   * @param bus true when the cardholder tapped off bus, false for subway
   */
  void tapOff(int[] end, String endLocation, boolean bus, boolean readEvent) {
    active = active && TransitSystem.systemOn;
    if (active) {
      // get starting time for last ride
      int[] start = history.get(history.size() - 1).start;
      boolean inActive = false;
      // didn't tap off and didn't tap in
      if ((onSubway && bus) || (onBus && !bus)) {
        // end previous trip
        cost = 0;
        Ride lastRide = history.get(history.size() - 1);
        int[] endTimes = new int[5];
        System.arraycopy(lastRide.start, 0, endTimes, 0, 3);
        endTimes[3] = 23;
        endTimes[4] = 59;
        endTrip(false, endTimes, lastRide.startLocation, true, onBus);
        // start a trip starting at time,location at tap off
        startTrip(bus, false, endLocation, end);
        // end trip
        endTrip(false, end, endLocation, true, bus);
      } else if (!onBus && !onSubway) {
        // when didn't tap in
        cost = 0;
        // start a trip starting at time,location at tap off
        startTrip(bus, false, endLocation, end);
        // end trip
        endTrip(false, end, endLocation, true, bus);
      } else {
        // check to see if in active time
        if (start[0] == activeTime[0]
                && start[1] == activeTime[1]
                && start[2] == activeTime[2]
                && (start[3] - activeTime[3]) < 2
            || (start[3] - activeTime[3] == 2 && start[4] < activeTime[4])) {
          inActive = true;
        } else {
          // clear the cost if not in active time
          cost = 0;
        }
        endTrip(inActive, end, endLocation, false, bus);
      }
<<<<<<< HEAD
      if (readEvent) {
=======
      if (Main.write) {
>>>>>>> aa3926ad7ca912f0cdc107f2fe0165243913e9b9
        System.out.println(
            "Card "
                + this.number
                + " taps off at "
                + endLocation
                + " at "
                + Arrays.toString(end)
                + ". The card balance is "
                + balance
                + ".");
      }
      if (bus) {
        onBus = false;
        onSubway = false;
      } else {
        onBus = false;
        onSubway = false;
      }
    } else {
<<<<<<< HEAD
      if (readEvent) {
=======
      if (Main.write) {
>>>>>>> aa3926ad7ca912f0cdc107f2fe0165243913e9b9
        System.out.println("Sorry the system is currently closed");
      }
    }
  }

  /**
   * load money to the card
   *
   * @param amount the amount that load to the card balance
   */
  void loadMoney(int amount) {
    active = active && TransitSystem.systemOn;
    if (active) {
      balance = balance + amount;
      if (Main.write) {
        System.out.println(
            "Adds $" + amount + " to card " + number + ". Card balance is now " + balance + ".");
      } else if(Main.write) {
        System.out.println("Sorry the system is currently closed");
      }
    }
  }
}
