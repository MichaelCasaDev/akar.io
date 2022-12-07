package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import server.net.ClientPacket;
import server.net.ClientPacket.StatusEnum;

public class ClientManager {
  private WindowStates windowStates;

  private Socket connection = null;
  private ObjectInputStream input;
  private ObjectOutputStream output;
  private ConnectionStates connected = ConnectionStates.DISCONNECTED;

  private String remoteHost;
  private int remotePort;

  private WindowManager windowManager;

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

  public void tryConnection() {
    (new Thread() {
      public void run() {
        while (connected != ConnectionStates.CONNECTED) {
          try {
            connected = ConnectionStates.CONNECTING;

            connection = new Socket(remoteHost, remotePort);
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());

            connected = ConnectionStates.CONNECTED;

            System.out.println("Connected to the server!");
            System.out.println(connection);

            output.writeObject(new ClientPacket(null, StatusEnum.CONNECT, new Date().getTime()));
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
    }).start();
  }

  public void disconnect() {
    try {
      input.close();
      output.close();
      connection.close();

      connected = ConnectionStates.DISCONNECTED;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public WindowManager getWindowManager() {
    return windowManager;
  }
}
