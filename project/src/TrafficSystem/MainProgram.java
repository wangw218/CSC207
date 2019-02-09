package TrafficSystem;

import TrafficSystem.CardStrategy.CardManager;
import TrafficSystem.CardStrategy.TicketManager;
import TrafficSystem.Holder.CardHolderManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/** The main class that constructs and runs the program. */
public class MainProgram extends Application {

  /** Transit system. */
  public static TransitSystem system;
  /** Password exists. */
  public static boolean passwordSet = false;

  /**
   * Construct new CardHolderManager, CardManager, ticketManager and system. Deserialize them if
   * they are serialized.
   *
   * @throws IOException Cannot read the file.
   * @throws ClassNotFoundException Cannot find the class.
   */
  private static void constructSystem() throws IOException, ClassNotFoundException {
    // Deserialize TransitSystem, or construct a new one if it does not exist.
    File dir = new File("phase2/Files/TrafficSystem.TransitSystem.ser");
    boolean exists = dir.exists();
    if (!exists) {
      system = new TransitSystem();
    } else {
      system =
          (TransitSystem) Serialization.deserialize("phase2/Files/TrafficSystem.TransitSystem.ser");
      if (system.passwordExists()) { // Whether it is the first time setting the password.
        passwordSet = true;
      }
      // Adds information after deserialization.
      CardHolderManager cardHolderManager = new CardHolderManager();
      cardHolderManager.addAllHolders();
      CardManager cardManager = new CardManager();
      cardManager.addAllCards();
      TicketManager ticketManager = new TicketManager();
      ticketManager.addAllTickets();
    }
  }

  /**
   * Starts the program.
   *
   * @param args PSVM args.
   */
  public static void main(String[] args) {
    SystemLogger.getInstance().log("FINE", "Opening a new system.");

    createFolder("phase2/Files/CardHolders");
    createFolder("phase2/Files/Cards");
    createFolder("phase2/Files/Tickets");

    launch(args);
  }

  /**
   * Create a new folder to save .ser files.
   *
   * @param name The path of the folder.
   */
  private static void createFolder(String name) {
    File folder = new File(name);
    if (!folder.exists()) {
      Boolean exist = folder.mkdir();
      SystemLogger.getInstance().log("FINE", "Create new folder " + name + ": " + exist);
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    constructSystem();

    Parent root = FXMLLoader.load(getClass().getResource("Controller/Dashboard.fxml"));
    primaryStage.setTitle("Traffic System");
    primaryStage.setScene(new Scene(root, 400, 400));
    primaryStage.show();
  }
}
