private class Ride {
  int[] start;
  int[] end = new int[5];
  String startLocation;
  String endLocation = "";
  double cost = 0;

  Ride(int[] startTime, String startLocation) {
    this.start = startTime;
    this.startLocation = startLocation;
  }

  void tapOff(int[] endTime, String endLocation) {
    end = endTime;
    this.endLocation = endLocation;
  }

  public String toString() {
    String startTime =  start[0] +"."+ start[1] +"."+ start[2] + "."+start[3] + "."+start[4];
    String endTime = end[0] + "."+end[1] +"."+ end[2] +"."+ end[3] + "."+end[4];
    String string = "enter at " + startLocation + " at " + startTime + ".";
    if (!endTime.equals("0.0.0.0.0")) {
      string += ". exit at " + endLocation + " at " + endTime;
    }
    return string;
  }
}
