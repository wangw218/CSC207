package TrafficSystem.CardStrategy;

import TrafficSystem.MainProgram;
import TrafficSystem.PublicTransport.Bus;
import TrafficSystem.PublicTransport.Ride;
import TrafficSystem.PublicTransport.Subway;
import TrafficSystem.PublicTransport.Trip;
import TrafficSystem.SystemLogger;

/** A class deal with card status. */
public class CardReader {
  /**
   * Uses ticket to tap in.
   *
   * @param number Ticket number.
   * @param bus Taps in on bus.
   * @param startLocation Taps in location.
   */
  public static String tapIn(String number, boolean bus, String startLocation) {
    MainProgram.system.updateStationVisited(startLocation);
    Ticket ticket = TicketManager.tickets.get(number);
    if ((bus && !ticket.bus) || (!bus && ticket.bus)) {
      SystemLogger.getInstance().log("WARNING", "Wrong ticket: " + number);
      return "Wrong ticket";
    } else if (ticket.used) {
      SystemLogger.getInstance().log("WARNING", "Ticket " + number + " has already been used.");
      return "Ticket has already been used";
    } else if (ticket.in) {
      SystemLogger.getInstance().log("WARNING", "Ticket " + number + " failed to tap in.");
      return "Ticket has been tapped in";
    } else if (!ticket.start.equals(startLocation)) {
      SystemLogger.getInstance()
          .log("WARNING", "Ticket " + number + " should be tap in at " + ticket.start + ".");
      return "Ticket should be tap in at " + ticket.start;
    }
    ticket.start = startLocation;
    ticket.in = true;
    ticket.modify();
    String type;
    if (bus) {
      type = "stop";
    } else {
      type = "station";
    }
    SystemLogger.getInstance()
        .log("INFO", "Ticket " + number + " taps in " + startLocation + " " + type);
    return "Tap in " + startLocation + " " + type;
  }

  /**
   * Uses ticket to tap off.
   *
   * @param number Ticket number.
   * @param bus Taps off on bus.
   * @param realEnd Tap off location.
   * @return Tap off status.
   */
  public static String tapOff(String number, boolean bus, String realEnd) {
    MainProgram.system.updateStationVisited(realEnd);
    Ticket ticket = TicketManager.tickets.get(number);
    int stop;
    String result;
    if (ticket.used) {
      SystemLogger.getInstance().log("WARNING", "Ticket " + number + " is used.");
      return "Ticket is used";
    } else if (!ticket.in) {
      SystemLogger.getInstance().log("WARNING", "Ticket " + number + " has not been tapped in.");
      return "Ticket has not been tapped in";
    } else {
      if (!bus) {
        if (!realEnd.equals(ticket.end)) {
          SystemLogger.getInstance()
              .log("WARNING", "Ticket " + number + " tap failed, wrong location.");
          return "Tap failed, Wrong location";
        }
        stop = Subway.getStop(ticket.start, realEnd);
      } else {
        if (!realEnd.equals(ticket.end)) {
          SystemLogger.getInstance()
              .log("WARNING", "Ticket " + number + " tap failed, wrong location.");
          return "Tap failed, Wrong location";
        }
        stop = Bus.getStop(ticket.start, realEnd);
      }
    }
    ticket.used = true;
    ticket.modify(stop);
    String type;
    if (bus) {
      type = "stop";
    } else {
      type = "station";
    }
    result = "Tap off " + realEnd + " " + type;
    SystemLogger.getInstance().log("INFO", "Ticket " + number + ": " + result);
    return result;
  }

  /**
   * Uses card to tap in.
   *
   * @param number Card number.
   * @param bus Taps in on bus.
   * @param startTime Time to tap in.
   * @param startLocation Tap in location.
   * @return Tap in information: currently card balance.
   */
  public static String tapIn(String number, boolean bus, int[] startTime, String startLocation) {
    MainProgram.system.updateStationVisited(startLocation);
    Card card = CardManager.cards.get(number);
    boolean valid;
    if (card instanceof MonthlyCard) {
      valid = ((MonthlyCard) card).checkValid(startTime);
    } else {
      valid = card.checkValid();
    }
    double totalCharge = 0;
    if (valid) {
      boolean poss = true;
      Trip lastTrip;
      if (card.history.size() != 0) {

        lastTrip = card.history.remove(card.history.size() - 1);
        // check if there is unfinished trip
        // didn't tap off last time
        if (card.onBus || card.onSubway) {
          double modify = lastTrip.endTrip(card.onBus, card.fs);
          card.modifyTrip();
          totalCharge += modify;
          poss = false;
          if ((card.onBus && bus) || (card.onSubway && !bus)) {
            SystemLogger.getInstance()
                .log(
                    "WARNING",
                    "Card " + number + " has an unfinished trip. You did not tap off last time.");
          }
        }
        // check if it is the same trip as previous one, if not start a new trip
        if (!lastTrip.sameTrip(startTime, startLocation) || !poss) {
          card.history.add(lastTrip);
          lastTrip = new Trip();
          card.addTrip(lastTrip);
        }
        if (totalCharge > card.balance) {
          totalCharge = totalCharge * 100;
          totalCharge = Math.round(totalCharge);
          totalCharge = (totalCharge / 100);
          card.changeBalance(totalCharge);
          SystemLogger.getInstance()
              .log("WARNING", "Card " + number + " has low balance, tap failed.");
          return "Low balance, tap failed";
        }
      } else {
        lastTrip = new Trip();
        card.addTrip(lastTrip);
      }
      Ride ride = new Ride(startTime, startLocation, bus);
      totalCharge += lastTrip.addRide(ride, card.fs, false);
      card.modifyTrip();
      card.history.add(lastTrip);
      // change status for card
      card.changeBalance(totalCharge);
      if (bus) {
        card.onBus = true;
        card.onSubway = false;
      } else {
        card.onBus = false;
        card.onSubway = true;
      }
      SystemLogger.getInstance()
          .log("INFO", "Card " + number + " taps in at: " + startLocation + ", on bus: " + bus);
      return ("balance" + card.balance + " cost:" + totalCharge);
    }
    SystemLogger.getInstance()
        .log(
            "INFO", "Card " + number + " taps in failed at: " + startLocation + ", on bus: " + bus);
    return "Tap in failed.";
  }

  /**
   * Uses card to tap off.
   *
   * @param number Card number.
   * @param bus Taps off on bus.
   * @param endTime Tap off time.
   * @param endLocation Tap off location.
   * @return Tap off information.
   */
  public static String tapOff(String number, boolean bus, int[] endTime, String endLocation) {
    MainProgram.system.updateStationVisited(endLocation);
    Card card = CardManager.cards.get(number);
    boolean valid;
    if (card instanceof MonthlyCard) {
      valid = ((MonthlyCard) card).checkValid(endTime);
    } else {
      valid = card.checkValid();
    }
    double totalCharge = 0;
    if (valid) {
      // if didn't tap off and didn't tap in
      if ((bus && card.onSubway) || (!bus && card.onBus)) {
        totalCharge += finish_and_start(bus, endTime, endLocation, card);
      }
      // if didn't tap in
      else if (!card.onBus && !card.onSubway) {
        Trip lastTrip = new Trip();
        card.addTrip(lastTrip);
        card.history.add(lastTrip);
        Ride ride = new Ride(endTime, endLocation, bus, true);
        totalCharge += lastTrip.addRide(ride, card.fs, true);
        card.modifyTrip();
        if ((card.onBus && bus) || (card.onSubway && !bus)) {
          SystemLogger.getInstance().log("WARNING", "Card " + number + " has tapped in twice.");
        }
      }
      Trip lastTrip = card.history.remove(card.history.size() - 1);
      // if on bus but not on the same line
      if (bus && card.onBus && !card.onSubway) {
        if (!Bus.same_line(
            lastTrip.rides.get(lastTrip.rides.size() - 1).startLocation, endLocation)) {
            SystemLogger.getInstance()
                    .log("WARNING", "Card " + number + " taps in and taps off on a different line.");
          card.history.add(lastTrip);
          totalCharge += finish_and_start(true, endTime, endLocation, card);
        }
      }
      card.history.add(lastTrip);
      double[] modify = lastTrip.endTrip(bus, card.fs, endTime, endLocation);
      card.modifyTrip();
      totalCharge += modify[1];
      card.history
          .get(card.history.size() - 1)
          .rides
          .get(card.history.get(card.history.size() - 1).rides.size() - 1)
          .tapOff(endTime, endLocation);
      card.changeBalance(totalCharge);
      card.changeStop((int) modify[0]);
      card.onSubway = false;
      card.onBus = false;
      card.modifyTrip();
      totalCharge = totalCharge * 100;
      totalCharge = Math.round(totalCharge);
      totalCharge = (totalCharge / 100);
      SystemLogger.getInstance()
          .log("INFO", "Card " + number + " taps off at: " + endLocation + ", on bus: " + bus);
      return ("balance" + card.balance + " cost:" + totalCharge);
    }
    SystemLogger.getInstance()
        .log("INFO", "Card " + number + " taps off failed at: " + endLocation + ", on bus: " + bus);
    return "Tap off failed";
  }

  /**
   * Calculates the total cost of a trip that failed to tap off before.
   *
   * @param bus It is on bus.
   * @param start Starting time.
   * @param startLocation Start location.
   * @param card Card information.
   * @return The total cost of tap in then tap in again.
   */
  private static double finish_and_start(
      boolean bus, int[] start, String startLocation, Card card) {
    double totalCharge = 0;
    Trip lastTrip = card.history.remove(card.history.size() - 1);
    totalCharge += lastTrip.endTrip(card.onBus, card.fs);
    card.modifyTrip();
    Ride ride = new Ride(start, startLocation, bus);
    card.history.add(lastTrip);
    lastTrip = new Trip();
    card.addTrip(lastTrip);
    totalCharge += lastTrip.addRide(ride, card.fs, true);
    card.modifyTrip();
    card.history.add(lastTrip);
    return totalCharge;
  }
}
