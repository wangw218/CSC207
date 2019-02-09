/**
 * Created by lindseyshorser on 2018-05-10.
 */

import java.util.ArrayList;

public class Player {

    private String name;
    private int rank;
    private ArrayList games;

    Player(String name, int rank){
        this.name = name;
        this.rank = rank;
        this.games = new ArrayList<Game>();
    }

    public String getName(){
        return this.name;
    }

    public int getRank(){
        return this.rank;
    }
    public void addGame(Game game){
        if (! this.games.contains(game))
            this.games.add(game);
            game.addPlayer(this);
    }

    public ArrayList getGames(){
        return games;
    }

    public boolean equals(Object obj){
        return obj instanceof Player && this.rank == ((Player) obj).rank && ((Player) obj).name.equals(this.name);
    }

    public String toString(){
        StringBuilder answer = new StringBuilder(this.name + ", " + this.rank);
        for(int i = 0; i < this.games.size(); i++){
            Game game = (Game)this.games.get(i);
            answer.append(System.lineSeparator() + game.getId());
        }
        return answer.toString();


    }

}
