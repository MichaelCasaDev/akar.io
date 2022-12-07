package server.managers;

import java.util.ArrayList;

import server.Main;
import server.entities.Player;
import server.utils.Pos;

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

  public Player getFullPlayerMe(Player player) {
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getUsername().equals(player.getUsername())) {
        return players.get(i);
      }
    }

    return null;
  }

  public void updatePlayer(Player otherPlayer) {
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getUsername().equals(otherPlayer.getUsername())) {
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

  public Player[] getPlayersAndGeneratePos(Player playerMe) {
    Player[] playersArr = new Player[players.size()];
    for (int i = 0; i < players.size(); i++) {
      playersArr[i] = players.get(i);
      if (playerMe != null && playersArr[i].getUsername().equals(playerMe.getUsername())) {
        boolean okPos = false;

        while (!okPos) {
          double randomX = Math.floor(Math.random() * 8800) + 1200;
          double randomY = Math.floor(Math.random() * 8800) + 1200;

          playersArr[i].setPos(new Pos((int) randomX, (int) randomY));

          if (Main.gManager.checkCollision(playersArr[i]) == -1) {
            okPos = true;
          }
        }
      }
    }

    return playersArr;
  }

}
