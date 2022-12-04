package client.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import server.entities.Food;
import server.entities.Player;
import server.entities.Spike;
import server.net.ClientPacket;
import server.net.ServerPacket;
import server.net.ClientPacket.StatusEnum;
import server.utils.Pos;

public class Gameplay extends JPanel implements ActionListener {
  private Rectangle outerArea;
  private JViewport vPort;
  public static int WIDTH = 1280;
  public static int HEIGHT = 768;

  private Player playerMe;
  private Player[] otherPlayers;
  private Food[] foods;
  private Spike[] spikes;
  private String avgPing;

  private boolean connected = false;
  private Socket connection = null;
  private ObjectInputStream input;
  private ObjectOutputStream output;

  private boolean okSendPck = false;

  private double scale = 1;

  public Gameplay() {
    Timer timer = new Timer(30, this);
    setFocusable(true);
    requestFocusInWindow();

    Dimension newSize = new Dimension(4000, 4000);
    outerArea = new Rectangle(0, 0, 4000, 4000);
    setPreferredSize(newSize);
    timer.start();

    byte[] array = new byte[8];
    new Random().nextBytes(array);
    String generatedString = new String(array, Charset.forName("UTF-8"));
    playerMe = new Player(new Pos(200, 300), new Color(0, 244, 244), generatedString);

    otherPlayers = new Player[0];
    spikes = new Spike[0];
    foods = new Food[0];

    tryConnection();
    recivePacketsFromServer();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    setBackground(new Color(240, 248, 255));

    if (connected) {
      try {
        if (okSendPck) {
          output.reset();
          output.writeObject(new ClientPacket(playerMe, StatusEnum.MOVE, new Date().getTime()));
          okSendPck = false;
        }

        drawMap(g2);
        drawFoods(g2);

        if (!playerMe.isDead()) {
          drawPlayerMe(g2);
        } else {
          System.out.println("YOU ARE DEAD");
        }
        drawPlayers(g2);
        drawSpikes(g2);
        drawInfo(g2);

        g2.draw(outerArea);
        g2.dispose();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void drawInfo(Graphics2D g2) {
    g2.setColor(new Color(255, 0, 0));
    g2.drawString("Player cords - (x: " + playerMe.getPos().getPosX() + ", y: " + playerMe.getPos().getPosY() + ")", 32,
        32);
    g2.drawString("Total FOODS - (" + foods.length + ")", 32,
        64);
    g2.drawString("Total PLAYERS - (" + otherPlayers.length + ")", 32,
        96);
    g2.drawString("Total SPIKES - (" + spikes.length + ")", 32,
        128);
    g2.drawString("Avg PING - (" + avgPing + ")", 32,
        160);
  }

  private void drawSpikes(Graphics2D g2) {
    for (int i = 0; i < spikes.length; i++) {
      try {
        File img = new File("src/client/spike.png");
        BufferedImage image = ImageIO.read(img);

        g2.drawImage(image, spikes[i].getPos().getPosX(), spikes[i].getPos().getPosY(),
            (int) (spikes[i].getSize() * scale),
            (int) (spikes[i].getSize() * scale), this);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void drawFoods(Graphics2D g2) {
    for (int i = 0; i < foods.length; i++) {
      if (foods[i] != null) {
        g2.setColor(foods[i].getColor());
        g2.fill(new Ellipse2D.Double(foods[i].getPos().getPosX(), foods[i].getPos().getPosY(),
            20 * scale, 20 * scale));
      }
    }
  }

  private void drawPlayers(Graphics2D g2) {
    for (int i = 0; i < otherPlayers.length; i++) {
      if (!otherPlayers[i].getUsername().equals(playerMe.getUsername()) && !otherPlayers[i].isDead()) {
        g2.setColor(otherPlayers[i].getColor());
        g2.fill(new Ellipse2D.Double(otherPlayers[i].getPos().getPosX(), otherPlayers[i].getPos().getPosY(),
            otherPlayers[i].getMass() * scale, otherPlayers[i].getMass() * scale));
      }
    }
  }

  private void drawMap(Graphics2D g2) {
    for (int i = 0; i < 4000; i += 40) {
      g2.drawRect(0, i, 4000, 1);
      g2.setColor(new Color(210, 210, 210));
    }

    for (int i = 0; i < 4000; i += 40) {
      g2.drawRect(i, 0, 1, 4000);
      g2.setColor(new Color(210, 210, 210));
    }
  }

  private void drawPlayerMe(Graphics2D g2) {
    g2.setColor(playerMe.getColor());
    g2.fill(new Ellipse2D.Double(playerMe.getPos().getPosX(), playerMe.getPos().getPosY(), playerMe.getMass() * scale,
        playerMe.getMass() * scale));
  }

  public void setvPort(JViewport vPort) {
    this.vPort = vPort;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Point mousePosition = getMousePosition();
    if (connected && mousePosition != null && !playerMe.isDead()) {
      double dx = mousePosition.x - playerMe.getPos().getPosX() - playerMe.getMass() / 2;
      double dy = mousePosition.y - playerMe.getPos().getPosY() - playerMe.getMass() / 2;

      double dxMul = Math.abs(dx) < playerMe.getMass() * 5 ? 4 : 7;
      double dyMul = Math.abs(dy) < playerMe.getMass() * 5 ? 4 : 7;

      if (dx * dx + dy * dy > 12) {
        double angle = Math.atan2(dy, dx);

        if (mousePosition.getX() < playerMe.getPos().getPosX() - playerMe.getMass()
            || mousePosition.getX() > playerMe.getPos().getPosX() + playerMe.getMass()
            || mousePosition.getY() < playerMe.getPos().getPosY() - playerMe.getMass()
            || mousePosition.getY() > playerMe.getPos().getPosY() + playerMe.getMass()) {
          playerMe.getPos().setPosX((int) (playerMe.getPos().getPosX() + (dxMul * Math.cos(angle))));
          playerMe.getPos().setPosY((int) (playerMe.getPos().getPosY() + (dyMul * Math.sin(angle))));

          Point view = new Point((int) playerMe.getPos().getPosX() - (WIDTH / 2) + (playerMe.getMass() / 2),
              (int) playerMe.getPos().getPosY() - (HEIGHT / 2) + (playerMe.getMass() / 2));
          vPort.setViewPosition(view);
        }
      }
    }

    repaint();
  }

  private void tryConnection() {
    (new Thread() {
      public void run() {
        while (!connected) {
          try {
            connection = new Socket(InetAddress.getLocalHost(), 7373);

            output = new ObjectOutputStream(connection.getOutputStream()); // invio al server
            input = new ObjectInputStream(connection.getInputStream()); // lettura dal server

            connected = true;

            System.out.println("Connected to the server!");
            System.out.println(connection);

            output.writeObject(new ClientPacket(playerMe, StatusEnum.CONNECT, new Date().getTime()));
            okSendPck = true;
          } catch (Exception ex) {
            System.out.println("Server offline!");
            System.out.println("Rety in 2 seconds...");
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

  private double calculateScale() {
    return 1;
  }

  private void recivePacketsFromServer() {
    (new Thread() {
      public void run() {
        while (true) {
          try {
            Object o = input.readObject();

            if (!(o instanceof ServerPacket)) {
              System.out.println("Unknown packet from server. Rejected!");
            } else {
              ServerPacket sPacket = (ServerPacket) o;
              okSendPck = false;

              spikes = sPacket.getSpikes();
              foods = sPacket.getFoods();
              otherPlayers = sPacket.getPlayers();
              avgPing = sPacket.getAvgPing();

              for (Player p : otherPlayers) {
                if (p.getUsername().equals(playerMe.getUsername())) {
                  playerMe.setMass(p.getMass());
                  playerMe.setDead(p.isDead());

                  scale = calculateScale();
                }
              }

              okSendPck = true;
            }

            Thread.sleep(1000 / 60); // 16pps (packets-per-second)
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }

}