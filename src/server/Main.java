package server;

import java.io.PrintStream;

import server.managers.GameManager;
import server.net.Server;
import server.views.BasicLog;

public class Main {
  private static BasicLog basicLog = new BasicLog();
  public static GameManager gManager = new GameManager();

  public static void main(String[] args) {
    System.setOut(new PrintStream(System.out) {
      public void println(String s) {
        basicLog.addLine(s);
        super.println(s);
      }
    });

    new Server();
  }
}