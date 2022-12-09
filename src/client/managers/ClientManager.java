package client.managers;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics2D;

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
  private Player[] otherPlayers;

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
        disconnect();
        tryConnection(StatusEnum.INFO);
        disconnect();

        windowManager.setjPanel(jPanels[1]);
        break;
      }
      case DEAD: {
        windowManager.setjPanel(jPanels[3]);
        break;
      }
      case PLAY: {
        disconnect();
        tryConnection(StatusEnum.CONNECT);

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

  public synchronized void tryConnection(StatusEnum status) {
    while (connected != ConnectionStates.CONNECTED) {
      try {
        connected = ConnectionStates.CONNECTING;

        connection = new Socket(remoteHost, remotePort);
        output = new ObjectOutputStream(connection.getOutputStream());
        input = new ObjectInputStream(connection.getInputStream());

        System.out.println("Connected to the server!");
        System.out.println(connection);

        output.writeObject(new ClientPacket(playerMe, status, new Date().getTime()));

        try {
          Object o = input.readObject();
          if (!(o instanceof ServerPacket)) {
            System.out.println("Unknown packet from server. Rejected!");
          } else {
            ServerPacket sPacket = (ServerPacket) o;
            otherPlayers = sPacket.getPlayers();

            if (status == StatusEnum.CONNECT) {
              for (Player p : otherPlayers) {
                if (p.getUsername().equals(playerMe.getUsername())) {
                  playerMe.setPos(p.getPos());
                }
              }
            }

            if (sPacket.isCanConnect() || status == StatusEnum.INFO) {
              connected = ConnectionStates.CONNECTED;
            }

            if (!sPacket.isCanConnect() && status == StatusEnum.CONNECT) {
              JOptionPane.showMessageDialog(null, "Lo username specificato è già in uso, scegliene un altro!", "Errore",
                  JOptionPane.ERROR_MESSAGE);

              windowManager.close();
              break;
            }
          }
        } catch (Exception e) {
          System.out.println("Errore ricezione dati");
          e.printStackTrace();
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

  public void generateClassifica(Graphics2D g2) {
    // draw table header
    g2.setColor(new Color(0, 0, 0));
    g2.drawString("#", 51, 175);
    g2.drawString("Username", 188, 175);
    g2.drawString("Punti", 354, 175);

    for (int i = 0; i < otherPlayers.length; i++) {
      if (i == 0) {
        g2.drawString("1.", 51, 221);
        g2.drawString(otherPlayers[i].getUsername(), 188, 221);
        g2.drawString(otherPlayers[i].getMass() + "", 354, 221);
      } else {
        g2.drawString(i + 2 + ".", 51, 221 + 32 * i - 1);
        g2.drawString(otherPlayers[i].getUsername(), 188, 221 + 32 * i - 1);
        g2.drawString(otherPlayers[i].getMass() + "", 354, 221 + 32 * i - 1);
      }
    }
  }
}
