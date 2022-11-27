package server.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import server.Main;
import server.entities.Food;
import server.entities.Player;
import server.entities.Spike;

public class Connection extends Thread {
  private Socket socket;
  private Server server;
  private ObjectInputStream input;
  private ObjectOutputStream output;

  public Connection(Socket socket, Server server) {
    try {
      this.socket = socket;
      this.server = server;

      this.output = new ObjectOutputStream(this.socket.getOutputStream());
      this.input = new ObjectInputStream(this.socket.getInputStream());

      this.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Main method to handle client in/out
   */
  @Override
  public void run() {
    try {
      while (true) {
        Object o = input.readObject();
        if (!(o instanceof ClientPacket)) {
          System.out.println("Invalid packet from client");
        } else {
          ClientPacket packet = (ClientPacket) o;

          switch (packet.getOperation()) {
            case MOVE: {
              switch (Main.gManager.checkCollision(packet.getPlayer())) {
                case 0: { // player
                  // check if other player is bigger or you
                  Player otherPlayer = Main.gManager.getOtherPlayerCollision(packet.getPlayer());
                  if (!(otherPlayer instanceof Player)) {
                    System.out.println("Error other player is not a player");
                    break;
                  }

                  if (otherPlayer.getMass() > packet.getPlayer().getMass()) {
                    Main.gManager.kill(packet.getPlayer());
                    otherPlayer.eatFood(packet.getPlayer().getMass() / 10);
                  } else {
                    Main.gManager.kill(otherPlayer);
                    packet.getPlayer().eatFood(otherPlayer.getMass() / 10);
                  }

                  Main.gManager.getpManager().updatePlayer(packet.getPlayer().getUsername(), packet.getPlayer());
                  Main.gManager.getpManager().updatePlayer(otherPlayer.getUsername(), otherPlayer);
                  break;
                }
                case 1: { // food
                  packet.getPlayer().eatFood(20);
                  Main.gManager.getpManager().updatePlayer(packet.getPlayer().getUsername(), packet.getPlayer());

                  break;
                }
                case 2: { // spike
                  Main.gManager.kill(packet.getPlayer());
                  Main.gManager.getpManager().updatePlayer(packet.getPlayer().getUsername(), packet.getPlayer());

                  break;
                }
                default: { // air
                  break;
                }
              }

              // Send informations back to client
              Player[] players = Main.gManager.getpManager().getPlayers();
              Food[] foods = Main.gManager.getfManager().getFoods();
              Spike[] spikes = Main.gManager.getsManager().getSpikes();
              boolean canConnect = true;
              String avgPing = calculateAvgPing(packet.getTime(), new Date().getTime());

              output.writeObject(new ServerPacket(players, foods, spikes, canConnect, avgPing));
              break;
            }
            case CONNECT: {
              // Send informations back to client
              Player[] players = null;
              Food[] foods = null;
              Spike[] spikes = null;
              boolean canConnect = Main.gManager.getpManager().isUsernameAvailable(packet.getPlayer().getUsername());
              String avgPing = null;

              output.writeObject(new ServerPacket(players, foods, spikes, canConnect, avgPing));
              break;
            }
            case DISCONNECT: {
              break;
            }
            case INFO: {
              // Send informations back to client
              Player[] players = Main.gManager.getpManager().getPlayers();
              Food[] foods = null;
              Spike[] spikes = null;
              boolean canConnect = false;
              String avgPing = null;

              output.writeObject(new ServerPacket(players, foods, spikes, canConnect, avgPing));
              break;
            }
            default: {
              break;
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Disconnect client from server
   */
  public void disconnect() {
    try {
      socket.close();
      this.server.removeConnection(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Send message to client
   * 
   * @param packet Packet to be sent to the client
   */
  public void sendMessage(ServerPacket packet) {
    try {
      this.output.writeObject(packet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String calculateAvgPing(long initialTime, long endTime) {
    return (initialTime - endTime) + "ms";
  }

}
