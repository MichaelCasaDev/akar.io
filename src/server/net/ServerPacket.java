package server.net;

import java.io.Serializable;
import java.util.Arrays;

import server.entities.Food;
import server.entities.Player;
import server.entities.Spike;

public class ServerPacket implements Serializable {
  private Player[] players;
  private Food[] foods;
  private Spike[] spikes;
  private boolean canConnect;
  private String avgPing;

  public ServerPacket(Player[] players, Food[] foods, Spike[] spikes, boolean canConnect, String avgPing) {
    this.players = players;
    this.foods = foods;
    this.spikes = spikes;
    this.canConnect = canConnect;
    this.avgPing = avgPing;
  }

  public Player[] getPlayers() {
    return this.players;
  }

  public Food[] getFoods() {
    return this.foods;
  }

  public Spike[] getSpikes() {
    return this.spikes;
  }

  public boolean isCanConnect() {
    return this.canConnect;
  }

  public String getAvgPing() {
    return this.avgPing;
  }

  @Override
  public String toString() {
    return "ServerPacket [players=" + Arrays.toString(this.players) + ", foods=" + Arrays.toString(this.foods)
        + ", spikes="
        + Arrays.toString(this.spikes) + ", avgPing=" + this.avgPing + "]";
  }

}
