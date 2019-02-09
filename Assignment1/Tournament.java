import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by lindseyshorser on 2018-05-10.
 */
public class Tournament {

    public static void main(String[] args) throws IOException {
        try (BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {
            String fileName = "PlayerList.txt";
            try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
                //Read the file.
                ArrayList<String> result = new ArrayList<>();
                String line = fileReader.readLine();
                while (line != null) {
                    String arr = line.trim();
                    result.add(arr);
                    line = fileReader.readLine();
                }

                //Ask the user to input and check

                System.out.println("Enter a game:");
                String gameId = keyboard.readLine().trim();

                while (! gameId.equals("exit")) {
                    Game game = new Game(gameId);
                    for (int i = 0; i < result.size(); i++){
                        String[] h = result.get(i).split("\\|");
                        for (int s = 0; s < h.length; s++ ) {
                            if (h[s].trim().equals(gameId) ) {
                                String name = h[0].split("\\,")[0].trim();
                                String rank = h[0].split("\\,")[1].trim();
                                Player player = new Player(name, Integer.valueOf(rank));
                                game.addPlayer(player);
                            }
                        }
                    }
                    if (game.getPlayer().size() != 0){
                        StringBuilder answer = new StringBuilder(game.getId() + " (");
                        for (int i = 0; i < game.getPlayer().size(); i++) {
                            answer.append(((Player) game.getPlayer().get(i)).getName() + ", ");
                        }
                        if (game.getPlayer().size() != 0) {
                            answer.deleteCharAt(answer.length() - 1);
                            answer.deleteCharAt(answer.length() - 1);
                        }
                        answer.append(")");
                        System.out.println(answer);
                    }else{
                        System.out.println("This is not a valid game");
                    }
                    System.out.println("Enter a game");
                    gameId = keyboard.readLine().trim();
                }

            }
        }
    }
}


