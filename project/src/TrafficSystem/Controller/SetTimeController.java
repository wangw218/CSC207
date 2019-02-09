package TrafficSystem.Controller;

import TrafficSystem.MainProgram;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

/** The scene to set time. */
public class SetTimeController extends Controller implements Initializable {
  /** Choose the year. */
  @FXML private ChoiceBox<String> yearBox;
  /** Choose the month. */
  @FXML private ChoiceBox<String> monthBox;
  /** Choose the day. */
  @FXML private ChoiceBox<String> dayBox;
  /** Choose the hour. */
  @FXML private ChoiceBox<String> hourBox;
  /** Choose the minute. */
  @FXML private ChoiceBox<String> minutesBox;
  /** The year. */
  private ArrayList<String> year = year();
  /** The month. */
  private ArrayList<String> month = timeArray(12);
  /** The day. */
  private ArrayList<String> day = timeArray(30);
  /** The hour. */
  private ArrayList<String> hour = timeArray(24);
  /** The minute. */
  private ArrayList<String> minute = timeArray(60);

  /** Generate the String with the correct format */
  private String reformat(int num) {
    if (num < 10) {
      return "0" + num;
    } else {
      return "" + num;
    }
  }
  /** Initialize the scene. */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    yearBox.getItems().addAll(year);
    yearBox.setValue("" + MainProgram.system.date[0]);
    monthBox.getItems().addAll(month);
    monthBox.setValue(reformat(MainProgram.system.date[1]));
    dayBox.getItems().addAll(day);
    dayBox.setValue(reformat(MainProgram.system.date[2]));
    hourBox.getItems().addAll(hour);
    hourBox.setValue(reformat(MainProgram.system.date[3]));
    minutesBox.getItems().addAll(minute);
    minutesBox.setValue(reformat(MainProgram.system.date[4]));
  }

  /** Confirm the time chose. */
  public void handleConfirm() {
    String years = yearBox.getValue();
    String months = monthBox.getValue();
    String days = dayBox.getValue();
    String hours = hourBox.getValue();
    String minutes = minutesBox.getValue();
    MainProgram.system.setDate(
        new int[] {
          Integer.parseInt(years),
          Integer.parseInt(months),
          Integer.parseInt(days),
          Integer.parseInt(hours),
          Integer.parseInt(minutes)
        });
    MainProgram.system.changed = true;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Information");
    alert.setContentText(
        "The current time is "
            + years
            + "--"
            + months
            + "--"
            + days
            + "  "
            + hours
            + ":"
            + minutes);

    alert.showAndWait();
  }

  /** Set up time. */
  private ArrayList<String> timeArray(int num) {
    DecimalFormat df = new DecimalFormat("00");
    ArrayList<String> times = new ArrayList<>();
    for (int i = 0; i < num; ++i) {
      if (num != 24 && num != 60) times.add(df.format(i + 1));
      else {
        times.add(df.format(i));
      }
    }
    return times;
  }

  /** Set up year. */
  private ArrayList<String> year() {
    ArrayList<String> times = new ArrayList<>();
    for (int i = 2010; i < 2021; ++i) {
      times.add(String.valueOf(i + 1));
    }
    return times;
  }
}
