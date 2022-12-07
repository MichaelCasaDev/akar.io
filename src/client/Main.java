package client;

import javax.swing.JPanel;

import client.views.Dead;
import client.views.Gameplay;
import client.views.Home;
import client.views.Startup;

public class Main {
  public static void main(String[] args) {
    // new WindowOLD();

    WindowManager windowManager = new WindowManager();
    ClientManager clientManager = new ClientManager();

    clientManager.setWindowManager(windowManager);

    Startup startup = new Startup(clientManager);
    Home home = new Home(clientManager);
    Dead dead = new Dead(clientManager);
    Gameplay gameplay = new Gameplay(clientManager);

    JPanel[] jPanels = { startup, home, gameplay, dead };
    clientManager.addPanels(jPanels);

    startup.launchAlert();
  }
}