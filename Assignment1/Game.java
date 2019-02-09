/**
 * Created by lindseyshorser on 2018-05-10.
 */

import java.util.ArrayList;

public class Game {

    private String id;
    private ArrayList players;

    public Game(String id){
        this.id = id;
        this.players = new ArrayList<Player>();
    }

    public String getId(){
        return this.id;
    }

    public ArrayList getPlayer(){
        return this.players;
    }

    public void addPlayer(Player player){
        if (! this.players.contains(player) ){
            this.players.add(player);
            player.addGame(this);
        }
    }


    public boolean hasPlayer(Player player){
        return this.players.contains(player);
    }

    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        Game other = (Game) obj;
        if ( !this.players.containsAll(other.players) || !other.players.containsAll(this.players))
            return false;
        return this.id.equals(other.id) ;
    }


    public boolean hasSamePlayers(Game game){
        for(int i = 0; i < this.players.size(); i++){
        if (game.players.contains(this.players.get(i)))
            return true;
        }
        return false;
}

    public String toString() {
        StringBuilder answer = new StringBuilder(this.id + " (");
        for (int i = 0; i < this.players.size(); i++) {
            answer.append(((Player) this.players.get(i)).getName() + ",");
        }
        if (this.players.size() != 0)
            answer.deleteCharAt(answer.length() - 1);
        answer.append(")");
        return answer.toString();
    }

}
