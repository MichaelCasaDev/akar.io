package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import javax.swing.JPanel;

import server.entities.Player;
import server.net.ClientPacket;
import server.net.ServerPacket;
import server.net.ClientPacket.StatusEnum;

public class ClientManager {
  private WindowStates windowStates;

  private Socket connection = null;
  private ObjectInputStream input;
  private ObjectOutputStream output;
  private ConnectionStates connected = ConnectionStates.DISCONNECTED;

  private String remoteHost;
  private int remotePort;
  private Player playerMe;

  private WindowManager windowManager;
  private JPanel[] jPanels;

  public enum WindowStates {
    MENU,
    DEAD,
    PLAY
  }

  public enum ConnectionStates {
    DISCONNECTED,
    CONNECTED,
    CONNECTING
  }

  public WindowStates getWindowStates() {
    return windowStates;
  }

  public void setWindowManager(WindowManager windowManager) {
    this.windowManager = windowManager;
  }

  public void setWindowStates(WindowStates windowStates) {
    this.windowStates = windowStates;

    System.out.println(windowStates);
    switch (this.windowStates) {
      case MENU: {
        windowManager.setjPanel(jPanels[1]);
        break;
      }
      case DEAD: {
        windowManager.setjPanel(jPanels[3]);
        break;
      }
      case PLAY: {
        disconnect();

        tryConnection();
        windowManager.setjPanel(jPanels[2]);
        break;
      }
    }
  }

  public Socket getConnection() {
    return connection;
  }

  public ObjectInputStream getInput() {
    return input;
  }

  public ObjectOutputStream getOutput() {
    return output;
  }

  public ConnectionStates getConnected() {
    return connected;
  }

  public String getRemoteHost() {
    return remoteHost;
  }

  public int getRemotePort() {
    return remotePort;
  }

  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
  }

  public void setRemotePort(int remotePort) {
    this.remotePort = remotePort;
  }

  public void setPlayerMe(Player playerMe) {
    this.playerMe = playerMe;
  }

  public Player getPlayerMe() {
    return playerMe;
  }

  public synchronized void tryConnection() {
    while (connected != ConnectionStates.CONNECTED) {
      try {
        connected = ConnectionStates.CONNECTING;

        connection = new Socket(remoteHost, remotePort);
        output = new ObjectOutputStream(connection.getOutputStream());
        input = new ObjectInputStream(connection.getInputStream());

        System.out.println("Connected to the server!");
        System.out.println(connection);

        output.writeObject(new ClientPacket(playerMe, StatusEnum.CONNECT, new Date().getTime()));

        try {
          Object o = input.readObject();
          if (!(o instanceof ServerPacket)) {
            System.out.println("Unknown packet from server. Rejected!");
          } else {
            ServerPacket sPacket = (ServerPacket) o;

            for (Player p : sPacket.getPlayers()) {
              if (p.getUsername().equals(playerMe.getUsername())) {
                playerMe.setPos(p.getPos());

                connected = ConnectionStates.CONNECTED;
              }
            }
          }
        } catch (Exception e) {
          System.out.println("Errore ricezione dati");
        }

      } catch (Exception ex) {
        System.out.println("Server offline!");
        System.out.println("Rety in 2 seconds...");
        connected = ConnectionStates.CONNECTING;
      }

      try {
        Thread.sleep(2000);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void disconnect() {
    try {
      if (input != null)
        input.close();
      if (output != null)
        output.close();
      if (connection != null)
        connection.close();

      connected = ConnectionStates.DISCONNECTED;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public WindowManager getWindowManager() {
    return windowManager;
  }

  public void addPanels(JPanel[] panels) {
    this.jPanels = panels;
  }
}
