package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
  private ServerSocket server;
  private ArrayList<Connection> connections;

  public Server() {
    try {
      server = new ServerSocket(7373, 5);

      System.out.println("Server attivo " + server.getLocalSocketAddress());

      run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void run() {
    try {
      while (true) {
        Socket con = server.accept();
        connections.add(new Connection(con, this));
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
