package server.managers;

import java.util.ArrayList;

import server.entities.Player;

public class PlayersManager {
  private ArrayList<Player> players;

  public PlayersManager() {
    players = new ArrayList<Player>();
  }

  public void addPlayer(Player p) {
    players.add(p);
  }

  public void removePlayer(Player p) {
    players.remove(p);
  }

  public int getTotalPlayers() {
    return players.size();
  }

  public Player[] getPlayers() {
    Player[] playersArr = new Player[players.size()];
    for (int i = 0; i < players.size(); i++) {
      playersArr[i] = players.get(i);
    }

    return playersArr;
  }

  public void updatePlayer(String username, Player otherPlayer) {
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getUsername().equals(username)) {
        players.set(i, otherPlayer);
      }
    }
  }

  public boolean isUsernameAvailable(String username) {
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getUsername().equals(username)) {
        return false;
      }
    }

    return true;
  }

}
