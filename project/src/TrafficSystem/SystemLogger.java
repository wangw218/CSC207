// Code adapted from Gary Kang:
// Source: https://github.com/garykang1476/Logger
// Retrieved in August 2018.
package TrafficSystem;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/** A class that will deal with the logger. */
public class SystemLogger {
  /** Logger for the transit system. */
  private static final Logger systemLogger = Logger.getLogger(SystemLogger.class.getName());
  /** The instance of SystemLogger. */
  private static SystemLogger instance = null;

  /**
   * Construct a new logger. Information that has level higher than INFO will be recorded in
   * log.txt.
   */
  private SystemLogger() {
    try {
      FileHandler file = new FileHandler("phase2/Files/Log/log.txt", true);
      file.setFormatter(new SimpleFormatter());
      systemLogger.addHandler(file);
      systemLogger.setLevel(Level.INFO);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Return current logger, or construct a new one.
   *
   * @return The current logger.
   */
  public static SystemLogger getInstance() {
    if (instance == null) {
      instance = new SystemLogger();
    }
    return instance;
  }

  /**
   * Record message in log base on level and context.
   *
   * @param level Level of the log, which contains "SEVERE", "WARNING", "INFO", and "FINE".
   * @param context The message being recorded.
   */
  public void log(String level, String context) {
    switch (level) {
      case "SEVERE":
        systemLogger.log(Level.SEVERE, context);
        break;
      case "WARNING":
        systemLogger.log(Level.WARNING, context);
        break;
      case "INFO":
        systemLogger.log(Level.INFO, context);
        break;
      case "FINE":
        systemLogger.log(Level.FINE, context);
        break;
    }
  }
}
