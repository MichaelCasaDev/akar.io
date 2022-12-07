package client;

import client.views.Home;
import client.views.Startup;

public class Main {
  public static void main(String[] args) {
    // new WindowOLD();

    WindowManager windowManager = new WindowManager();
    ClientManager clientManager = new ClientManager();

    clientManager.setWindowManager(windowManager);

    Home home = new Home(clientManager, null);
    new Startup(clientManager, home);

  }
}