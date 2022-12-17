package server.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
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
  private Player mePlayer;

  public Connection(Socket socket, Server server, int connId) {
    try {
      this.socket = socket;
      this.server = server;
      this.setName("#" + connId);

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
          System.out.println("[CLIENT " + Instant.ofEpochSecond(new Date().getTime() / 1000)
              + "] | Invalid packet from client. Rejecting it!");
        } else {
          ClientPacket packet = (ClientPacket) o;

          switch (packet.getOperation()) {
            case MOVE: {
              switch (Main.gManager.checkCollision(packet.getPlayer())) {
                case 0: { // player
                  System.out.println(
                      "[CLIENT " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | ("
                          + packet.getPlayer().getUsername()
                          + ") Player is colliding with another player!");
                  Player otherPlayer = Main.gManager.getOtherPlayerCollision(packet.getPlayer());

                  // check if other player is bigger or you
                  if (!(otherPlayer instanceof Player)) {
                    System.out.println(
                        "[CLIENT " + Instant.ofEpochSecond(new Date().getTime()) + "] | ("
                            + packet.getPlayer().getUsername() + ") Error other player is not a player!");
                    break;
                  }

                  // Other player is bigger
                  if (otherPlayer.getMass() > packet.getPlayer().getMass() + 10) {
                    Main.gManager.kill(packet.getPlayer());
                    otherPlayer.eatFood(packet.getPlayer().getMass() / 5);

                    // You are bigger
                  } else if (otherPlayer.getMass() < packet.getPlayer().getMass() + 10) {
                    Main.gManager.kill(otherPlayer);
                    packet.getPlayer().eatFood(otherPlayer.getMass() / 5);
                  }

                  // Update users
                  Main.gManager.getpManager().updatePlayer(packet.getPlayer());
                  Main.gManager.getpManager().updatePlayer(otherPlayer);

                  break;
                }
                case 1: { // food
                  System.out
                      .println("[CLIENT " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | ("
                          + packet.getPlayer().getUsername() + ") Player is colliding with food!");

                  Food otherFood = Main.gManager.getOtherFoodCollision(packet.getPlayer());
                  if (!otherFood.isEat()) {
                    Main.gManager.getfManager().eatFood(otherFood);
                    packet.getPlayer().eatFood(2);
                  }

                  // Update user
                  Main.gManager.getpManager().updatePlayer(packet.getPlayer());

                  break;
                }
                case 2: { // spike
                  System.out.println(
                      "[CLIENT " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | ("
                          + packet.getPlayer().getUsername()
                          + ") Player is colliding with a spike!");

                  if (packet.getPlayer().getMass() > 60) {
                    Main.gManager.kill(packet.getPlayer());
                  }

                  // Update user
                  Main.gManager.getpManager().updatePlayer(packet.getPlayer());

                  break;
                }
                default: { // air
                  // map borders are managed client-side
                  Main.gManager.getpManager().updatePlayer(packet.getPlayer());

                  break;
                }
              }

              // Send informations back to client
              Player[] players = Main.gManager.getpManager().getPlayers();
              Food[] foods = Main.gManager.getfManager().getFoods();
              Spike[] spikes = Main.gManager.getsManager().getSpikes();
              boolean canConnect = true;
              String avgPing = calculateAvgPing(packet.getTime(), new Date().getTime());

              this.mePlayer = packet.getPlayer();
              output.writeObject(new ServerPacket(players, foods, spikes, canConnect, avgPing));

              break;
            }
            case CONNECT: {
              System.out.println("[CLIENT " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | ("
                  + packet.getPlayer().getUsername() + ") Connecting new player...");
              // Send informations back to client
              Food[] foods = Main.gManager.getfManager().getFoods();
              Spike[] spikes = Main.gManager.getsManager().getSpikes();
              boolean canConnect = Main.gManager.getpManager().isUsernameAvailable(packet.getPlayer().getUsername());
              String avgPing = calculateAvgPing(packet.getTime(), new Date().getTime());

              if (canConnect) {
                Main.gManager.getpManager().addPlayer(packet.getPlayer());
                System.out.println("[CLIENT " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | ("
                    + packet.getPlayer().getUsername() + ") Player connected!");
              } else {
                System.out.println(
                    "[CLIENT " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | ("
                        + packet.getPlayer().getUsername() + ") Player rejected, username already in use!");
              }

              Player[] players = Main.gManager.getpManager().getPlayersAndGeneratePos(packet.getPlayer());

              this.mePlayer = Main.gManager.getpManager().getFullPlayerMe(packet.getPlayer());
              output.writeObject(new ServerPacket(players, foods, spikes, canConnect, avgPing));

              break;
            }
            case DISCONNECT: {
              disconnect();

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
      disconnect();
    }
  }

  /**
   * Disconnect client from server
   */
  public void disconnect() {
    try {
      System.out.println("[SERVER " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Closing connection "
          + this.getName() + "...");

      socket.close();
      this.server.removeConnection(this);
      Main.gManager.getpManager().removePlayer(this.mePlayer);
      System.out.println("[SERVER " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Connection "
          + this.getName() + " has been closed!");
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
      output.reset();
      output.writeObject(packet);
    } catch (IOException e) {
      e.printStackTrace();
      disconnect();
    }
  }

  private String calculateAvgPing(long initialTime, long endTime) {
    return (initialTime - endTime) + "ms";
  }

}
