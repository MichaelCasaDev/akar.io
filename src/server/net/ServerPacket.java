package server.net;

import java.io.Serializable;

import server.entities.Food;
import server.entities.Player;
import server.entities.Spike;

public class ServerPacket implements Serializable {
  private Player[] players;
  private Food[] foods;
  private Spike[] spikes;

  public ServerPacket(Player[] players, Food[] foods, Spike[] spikes) {
    this.players = players;
    this.foods = foods;
    this.spikes = spikes;
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

}
