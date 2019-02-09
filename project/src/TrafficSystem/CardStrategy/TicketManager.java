package TrafficSystem.CardStrategy;

import TrafficSystem.MainProgram;
import TrafficSystem.Serialization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** A class that manages all tickets. */
public class TicketManager {
  /** Total ticket in TicketManager. */
  static int totalTickets = 0;
  /** Contains all Ticket. */
  static Map<String, Ticket> tickets = new HashMap<>();

  /**
   * Adds a ticket into tickets.
   *
   * @param ticketNumber Ticket number.
   * @param ticket Ticket.
   */
  static void addTicket(String ticketNumber, Ticket ticket) {
    tickets.put(ticketNumber, ticket);
    totalTickets += 1;
  }

  /**
   * Checks whether the ticket is valid or not.
   *
   * @param number Ticket number.
   * @return The valid ticket.
   */
  public static boolean validTicket(String number) {
    for (String ticketNumber : tickets.keySet()) {
      if (ticketNumber.equals(number)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds all existing tickets into TicketManager.
   *
   * @throws IOException Failed to read the file.
   * @throws ClassNotFoundException Failed to find the class.
   */
  public void addAllTickets() throws IOException, ClassNotFoundException {
    String path = "phase2/Files/Tickets/";
    File file = new File(path);
    File[] fs = file.listFiles();
    assert fs != null;
    for (File f : fs) {
      Ticket ticket = null;
      if (!f.isDirectory()) ticket = (Ticket) Serialization.deserialize(f.getAbsolutePath());
      assert ticket != null;
      tickets.put(ticket.number, ticket);
      ticket.addObserver(MainProgram.system);
      totalTickets += 1;
    }
  }
}
