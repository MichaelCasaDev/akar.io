package server.net;

import java.io.Serializable;

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
    return players;
  }

  public Food[] getFoods() {
    return foods;
  }

  public Spike[] getSpikes() {
    return spikes;
  }

  public boolean isCanConnect() {
    return canConnect;
  }

  public String getAvgPing() {
    return avgPing;
  }

}
