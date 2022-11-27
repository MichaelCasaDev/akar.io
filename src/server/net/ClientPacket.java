package server.net;

import java.io.Serializable;

import server.entities.Player;

public class ClientPacket implements Serializable {
  private Player player;
  private StatusEnum operation;

  public static enum StatusEnum {
    MOVE,
    CONNECT,
    DISCONNECT,
    INFO
  }

  public ClientPacket(Player player, StatusEnum operation) {
    this.player = player;
    this.operation = operation;
  }

  public Player getPlayer() {
    return player;
  }

  public StatusEnum getOperation() {
    return operation;
  }

}
