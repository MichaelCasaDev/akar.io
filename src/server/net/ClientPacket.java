package server.net;

import java.io.Serializable;

import server.entities.Player;

public class ClientPacket implements Serializable {
  private Player player;
  private StatusEnum operation;
  private long time;

  public static enum StatusEnum {
    MOVE,
    CONNECT,
    DISCONNECT,
    INFO
  }

  public ClientPacket(Player player, StatusEnum operation, long time) {
    this.player = player;
    this.operation = operation;
    this.time = time;
  }

  public Player getPlayer() {
    return this.player;
  }

  public StatusEnum getOperation() {
    return this.operation;
  }

  public long getTime() {
    return this.time;
  }

}
