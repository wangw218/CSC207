package TrafficSystem.Holder;

import TrafficSystem.Serialization;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CardHolderManager {
  /** All users in the system. */
  public static Map<String, CardHolder> users = new HashMap<>();

  /**
   * Add a holder to CardHolderManager.
   *
   * @param email email for Cardholder.
   * @param holder Cardholder.
   */
  static void addHolder(String email, CardHolder holder) {
    users.put(email, holder);
  }

  /**
   * Determines if email is valid or not.
   *
   * @param email email for Cardholder.
   * @return email is a valid email address.
   */
  public static boolean validEmail(String email) {
    for (String userEmail : users.keySet()) {
      if (email.equals(userEmail)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Add all holders back into CardholderManager.
   *
   * @throws IOException Failed to find the file.
   * @throws ClassNotFoundException Failed to find the class.
   */
  public void addAllHolders() throws IOException, ClassNotFoundException {
    String path = "phase2/Files/CardHolders/";
    File file = new File(path);
    File[] fs = file.listFiles();
    CardHolder user = null;
    assert fs != null;
    for (File f : fs) {
      if (!f.isDirectory()) user = (CardHolder) Serialization.deserialize(f.getAbsolutePath());
      assert user != null;
      users.put(user.getEmail(), user);
    }
  }
}
