package TrafficSystem;

import java.io.*;

/** A class to serialize and deserialize. */
public class Serialization {
  /**
   * Serialize objects.
   *
   * @param fileName Path to serialize.
   * @param obj Object needs to be serialized.
   */
  public static void serialize(String fileName, Object obj) {
    try {
      FileOutputStream file = new FileOutputStream(fileName);
      ObjectOutputStream out = new ObjectOutputStream(file);
      out.writeObject(obj);
      out.close();
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
      SystemLogger.getInstance().log("WARNING", "Failed to serialize.");
    }
  }

  /**
   * Deserialize objects.
   *
   * @param fileName Path of file to deserialize.
   * @return Result of deserialization.
   * @throws IOException Failed to find the file.
   * @throws ClassNotFoundException Failed to find the class.
   */
  public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
    FileInputStream file = new FileInputStream(fileName);
    ObjectInputStream in = new ObjectInputStream(file);
    Object result = in.readObject();
    in.close();
    file.close();
    return result;
  }
}
