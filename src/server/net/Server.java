package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Server {
  private ServerSocket server;
  private ArrayList<Connection> connections;
  private int connectionsCounter = 0;

  public Server() {
    try {
      server = new ServerSocket(7373, 5);
      connections = new ArrayList<Connection>();

      System.out.println("[SERVER " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Server is online! - "
          + server.getLocalSocketAddress());

      run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void run() {
    try {
      while (true) {
        System.out.println(
            "[SERVER " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Waiting for new connections...");

        Socket con = server.accept();
        connections.add(new Connection(con, this, ++connectionsCounter));

        System.out.println("[SERVER " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Connection #"
            + connectionsCounter + " accepted!");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Remove a client from the list of connections
   */
  public void removeConnection(Connection con) {
    this.connections.remove(con);
    System.out.println("[SERVER " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Connection "
        + con.getName() + "closed succesfully!");
  }

  /**
   * Send a broadcast message to all clients
   * 
   * @param packet Packet to be broadcasted
   */
  public void broadcast(ServerPacket packet) {
    for (Connection con : this.connections) {
      con.sendMessage(packet);
    }
  }
}
