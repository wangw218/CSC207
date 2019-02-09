package TrafficSystem.CardStrategy;

import TrafficSystem.MainProgram;
import TrafficSystem.PublicTransport.Subway;
import TrafficSystem.Serialization;

import java.io.Serializable;

/** A class stores information and set changes to tickets. */
public class Ticket extends java.util.Observable implements Serializable {
  /** Ticket number. */
  public String number;
  /** Start location. */
  String start;
  /** End location. */
  String end;
  /** Ticket is in used. */
  boolean in = false;
  /** The ticket is used. */
  boolean used = false;
  /** On bus. */
  boolean bus;
  /** Fare strategy. */
  private double fare;

  /**
   * Constructs a new Ticket.
   *
   * @param start Start location.
   * @param end End location.
   * @param bus Bus trip or not.
   */
  public Ticket(String start, String end, boolean bus) {
    FareStrategy fs = new NormalStrategy();
    this.bus = bus;

    int digit = (int) (Math.log10(TicketManager.totalTickets + 1) + 1);
    number = "t" + repeat(8 - digit) + TicketManager.totalTickets;
    if (bus) {
      fare = fs.calculateBusFare();
    } else {
      int stop = Subway.getStop(start, end);
      fare = fs.calculateSubwayFare(stop);
    }
    addObserver(MainProgram.system);
    this.start = start;
    this.end = end;
    TicketManager.addTicket(number, this);
    serialize();
  }

  /**
   * Modifies the trip to a new stop.
   *
   * @param stop The new stop.
   */
  void modify(int stop) {
    setChanged();
    double[] out = new double[2];
    out[0] = stop;
    out[1] = fare;
    notifyObservers(out);
    serialize();
  }

  /** Modifies the trip. */
  void modify() {
    setChanged();
    double[] out = new double[2];
    notifyObservers(out);
    serialize();
  }

  /** Serializes tickets information. */
  private void serialize() {
    Serialization.serialize("phase2/Files/Tickets/" + number + ".ser", this);
  }

  /**
   * A helper method to add 0s into ticket number.
   *
   * @param times Repeating times.
   * @return Repeating 0s of ticket number.
   */
  private String repeat(int times) {
    return new String(new char[times]).replace("\0", "0");
  }
}
