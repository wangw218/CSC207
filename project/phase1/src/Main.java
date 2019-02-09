import java.io.*;

public class Main {
    private static TransitSystem system =
            new TransitSystem("phase1/stations.txt", "phase1/stops.txt");

    public static void main(String[] args) {
        readEvents("phase1/preEvent.txt");
        readEvents("phase1/events.txt");
    }

    /**
     * Reads and process events in the txt file.
     *
     * @param fileName The txt file has to be read.
     */
    private static void readEvents(String fileName) {
        boolean write = true;
        // Read events in event.txt
        try {

            // Forbids outputs if the file is preEvent.txt
            if (fileName.equals("phase1/preEvent.txt")) {
                //System.out.close();
                write = false;

            }

            // Read current events
            BufferedReader fileReader =
                    new BufferedReader(new FileReader(fileName));

            // Write previous events
            PrintWriter preEvent =
                    new PrintWriter(new BufferedWriter(new FileWriter("phase1/preEvent.txt", true)));

            String line = fileReader.readLine(); // Read the first line

            while (line != null) {
                if (write) {
                    preEvent.println(line); // Write a new line
                    preEvent.flush();
                }

                // Skips and read a new line if line is empty or it is a comment.
                if (line.isEmpty() || line.contains("//")) {
                    line = fileReader.readLine();
                    if (write) {
                        deleteEvent();
                    }
                    continue;
                }

                // Processes the current event.
                processFile(line);

                // Delete the current event.
                if (write) {
                    deleteEvent();
                }

                // Read a new line.
                line = fileReader.readLine();
            }

            if (write) {
                // Delete the last event
                deleteEvent();
            }


        } catch (IOException e) {
            System.out.println("Event IO Exception!");
        }
    }

    /**
     * Process the current event.
     *
     * @param line The line that needed to be processed.
     */
    private static void processFile(String line) {
        String[] sepInfo = line.split("[|]"); // Splits information

        String actions = sepInfo[1]; // What does human/card do

        if (sepInfo[0].trim().matches("[0-9]+")) { // Card does actions
            String card = sepInfo[0].trim(); // Getting card number

            // Adds money.
            if (actions.contains("adds")) {
                int money = Integer.parseInt(sepInfo[2].split("[$]")[1].trim());
                Card curCard = TransitSystem.cards.get(card);
                curCard.loadMoney(money);
            }

            // Enters/leaves a stop/station.
            else if (actions.contains("taps in") || actions.contains("taps off")) {
                Card curCard = TransitSystem.cards.get(card);
                String location = sepInfo[2].trim();
                Boolean onBus = sepInfo[2].contains("Stop");
                int[] time = readDate(sepInfo[3]);
                tap(sepInfo[1], time, location, curCard, onBus);
            }

        } else if (sepInfo[0].contains(",")) { // CardHolder does actions.
            String []userInfo = sepInfo[0].split(","); // Split name and email
            String userName = userInfo[0].trim();
            String userEmail = userInfo[1].trim();
            CardHolder holder = getHolder(userEmail, userName);

            // Buys a card.
            if (actions.contains("buys a card")) {
                // Create a holder and a new card
                holder.addCard();
            }

            // Removes a card.
            else if (actions.contains("removes")) {
                holder.removeCard(getCardNumber(sepInfo[2]));
            }

            // Changes user name.
            else if (actions.contains("changes name")) {
                holder.changeName(sepInfo[2].trim());
            }

            // Suspends a card.
            else if (actions.contains("suspends")) {
                holder.suspendCard(getCardNumber(sepInfo[2]));
            }

            // Activates a card.
            else if (actions.contains("reactivates")) {
                holder.activateCard(getCardNumber(sepInfo[2]));
            }

            // Views trip
            else if (actions.contains("views")) {
                holder.viewTrip();
            }

            // Tracks the average transit
            else if (actions.contains("tracks")) {
                holder.trackAverage();
            }

        } else if (sepInfo[0].contains("Admin")) { // Admin doing actions

            // Opens the system
            if (actions.contains("opens")) {
                TransitSystem.date = readDate(sepInfo[2]);
                system.openSystem();
            }

            // Closes the system
            else if (actions.contains("closes")) {
                TransitSystem.date = readDate(sepInfo[2]);
                system.closeSystem();
            }

            // Prints the daily report
            else if (actions.contains("prints the daily report")) {
                System.out.println(system.dailyReport());
            }

        } else if (sepInfo[0].contains("Hardware")) { // Hardware doing actions

            // Malfunctions
            if (actions.contains("malfunctions")) {
                TransitSystem.date = readDate(sepInfo[2]);
                system.openSystem();
            }

            // Recovers
            else if (actions.contains("recovers")) {
                TransitSystem.date = readDate(sepInfo[2]);
                system.closeSystem();
            }
        }
    }

    /**
     * Gets the card holder.
     *
     * @param email User's email.
     * @param name User's name.
     * @return The current card holder.
     */
    private static CardHolder getHolder(String email, String name) {
        if (!TransitSystem.users.containsKey(email)) {
            CardHolder newHolder = new CardHolder(email, name);
            TransitSystem.users.put(email, newHolder);
            return newHolder;
        } else {
            return TransitSystem.users.get(email);
        }
    }

    /**
     * Get the current card number in info.
     *
     * @param info String that contains card number.
     * @return Current card number.
     */
    private static String getCardNumber(String info) {
        StringBuilder cardNum = new StringBuilder();
        for (int i = 0; i < info.length(); i++) {
            String curChar = info.substring(i, i+1);
            if (curChar.matches("[0-9]+")) {
                cardNum.append(curChar);
            }
        }
        return cardNum.toString();
    }

    /**
     * Reads date in info.
     *
     * @param info String that contains date.
     * @return Current date.
     */
    private static int[] readDate(String info) {
        String[] strDate = info.split("on")[1].trim().split(" ");
        int[] date = new int[strDate.length];

        for(int i=0;i<strDate.length;i++){
            date[i]=Integer.parseInt(strDate[i]);
        }

        return date;
    }

    /**
     * Taps in and taps off the bus/subway.
     *
     * @param tapStatus Card status- taps in or taps off.
     * @param time Taps in/off time.
     * @param location Taps in/off location.
     * @param card The card tapping in/off.
     * @param onBus The holder is on bus or not.
     */
    private static void tap(String tapStatus, int[] time, String location,
                            Card card, boolean onBus) {
        if (tapStatus.contains("taps in")) {
            card.tapIn(onBus, time, location);
        } else {
            card.tapOff(time, location, onBus);
        }
    }

    /**
     * Delete lines that has been read.
     */
    private static void deleteEvent() {
        StringBuilder tempFile = new StringBuilder();

        try {
            BufferedReader fileReader =
                    new BufferedReader(new FileReader("phase1/events.txt"));
            // Starting from the second line.
            fileReader.readLine();
            String line = fileReader.readLine();

            while (line != null) {
                tempFile.append(line).append("\n");
                line = fileReader.readLine();
            }
            fileReader.close();

            BufferedWriter fileWriter  =
                    new BufferedWriter(new FileWriter("phase1/events.txt",false));
            fileWriter.write(tempFile.toString());
            fileWriter.close();

        } catch (IOException e) {
            // Sometimes the file will read too fast and it may catch io exception
            // Adds a new BufferedWriter as a new buffer, in order to prevent file reads
            // too fast and the writer cannot access it.
            try {
                BufferedWriter fileWriter =
                        new BufferedWriter(new FileWriter("phase1/events.txt", false));
                fileWriter.write(tempFile.toString());
                fileWriter.close();
            } catch (IOException ee) {
                System.out.println("Delete IO Exception.");
            }
        }
    }
}
