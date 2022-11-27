package server;

import server.managers.GameManager;
import server.net.Server;

public class Main {
  public static GameManager gManager = new GameManager();

  public static void main(String[] args) {
    new Server();
  }
}