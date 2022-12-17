package client.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.*;

import client.managers.ClientManager;
import client.managers.ClientManager.ConnectionStates;
import client.managers.ClientManager.WindowStates;

import java.awt.BasicStroke;

import server.entities.Food;
import server.entities.Player;
import server.entities.Spike;
import server.net.ClientPacket;
import server.net.ServerPacket;
import server.net.ClientPacket.StatusEnum;

public class Gameplay extends JPanel implements ActionListener {
  private Rectangle outerArea;
  private JViewport vPort;
  public static int WIDTH = 1280;
  public static int HEIGHT = 768;

  private Player[] otherPlayers;
  private Food[] foods;
  private Spike[] spikes;
  private String avgPing;

  private double dx;
  private double dy;
  private double scale = 1;

  private ClientManager clientManager;
  private boolean okSendPck = true; // Make client to send packets to server after have elaborated the ones received
                                    // from the server

  public Gameplay(ClientManager clientManager) {
    this.clientManager = clientManager;

    Timer timer = new Timer(60, this);
    timer.start();

    setFocusable(true);
    requestFocusInWindow();
    setPreferredSize(new Dimension(10000, 10000));// 1000 - 9000

    this.outerArea = new Rectangle(0, 0, 10000, 10000);
    this.otherPlayers = new Player[0];
    this.spikes = new Spike[0];
    this.foods = new Food[0];

    recivePacketsFromServer();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    setBackground(new Color(240, 248, 255));

    if (clientManager.getConnected() == ConnectionStates.CONNECTED) {
      try {
        if (okSendPck) {
          clientManager.getOutput().reset();
          clientManager.getOutput()
              .writeObject(new ClientPacket(clientManager.getPlayerMe(), StatusEnum.MOVE, new Date().getTime()));

          okSendPck = false;
        }

        drawMap(g2);
        drawFoods(g2);

        if (!clientManager.getPlayerMe().isDead()) {
          drawPlayerMe(g2);
        } else {
          clientManager.setWindowStates(WindowStates.DEAD);
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

  public void setvPort(JViewport vPort) {
    this.vPort = vPort;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Point mousePosition = getMousePosition();
    if (mousePosition != null) {
      this.dx = mousePosition.x;
      this.dy = mousePosition.y;
    }

    if (clientManager.getConnected() == ConnectionStates.CONNECTED && clientManager.getPlayerMe() != null
        && !clientManager.getPlayerMe().isDead()) {
      double dx = this.dx - clientManager.getPlayerMe().getPos().getPosX()
          - (clientManager.getPlayerMe().getMass() * scale) / 2;
      double dy = this.dy - clientManager.getPlayerMe().getPos().getPosY()
          - (clientManager.getPlayerMe().getMass() * scale) / 2;

      double dxMul = Math.abs(dx) < (clientManager.getPlayerMe().getMass() / 2) * 5 ? 3 : 5;
      double dyMul = Math.abs(dy) < (clientManager.getPlayerMe().getMass() / 2) * 5 ? 3 : 5;

      if (dx * dx + dy * dy > 10) {
        double angle = Math.atan2(dy, dx);
        int newPosY = (int) (clientManager.getPlayerMe().getPos().getPosY() + (dyMul * Math.sin(angle)));
        int newPosX = (int) (clientManager.getPlayerMe().getPos().getPosX() + (dxMul * Math.cos(angle)));

        if ((clientManager.getPlayerMe().getMass() + clientManager.getPlayerMe().getPos().getPosY() <= 9000
            && -clientManager.getPlayerMe().getMass() + clientManager.getPlayerMe().getPos().getPosY() >= 1000)

            && (clientManager.getPlayerMe().getMass() + clientManager.getPlayerMe().getPos().getPosX() <= 9000
                && -clientManager.getPlayerMe().getMass() + clientManager.getPlayerMe().getPos().getPosX() >= 1000)) {
          clientManager.getPlayerMe().getPos().setPosX(newPosX);
          clientManager.getPlayerMe().getPos().setPosY(newPosY);

          updateViewPosition();
        } else {
          if (newPosY < 9000 && newPosX < 9000 && newPosY > 1000 && newPosX > 1000) {
            clientManager.getPlayerMe().getPos().setPosX(newPosX);
            clientManager.getPlayerMe().getPos().setPosY(newPosY);

            updateViewPosition();
          }
        }
      }
    }

    repaint();
  }

  private void drawInfo(Graphics2D g2) {
    g2.setColor(new Color(255, 0, 0));
    g2.drawString(
        "Player cords - (x: " + clientManager.getPlayerMe().getPos().getPosX() + ", y: "
            + clientManager.getPlayerMe().getPos().getPosY() + ")",
        32,
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
      if (spikes[i].getPos().getPosX() < clientManager.getPlayerMe().getPos().getPosX() + (WIDTH / 1.8)
          && spikes[i].getPos().getPosX() > clientManager.getPlayerMe().getPos().getPosX() - (WIDTH / 1.8)
          && spikes[i].getPos().getPosY() < clientManager.getPlayerMe().getPos().getPosY() + (HEIGHT / 1.8)
          && spikes[i].getPos().getPosY() > clientManager.getPlayerMe().getPos().getPosY() - (HEIGHT / 1.8)) {
        try {
          File img = new File("src/client/imgs/spike.png");
          BufferedImage image = ImageIO.read(img);

          g2.drawImage(image, spikes[i].getPos().getPosX(), spikes[i].getPos().getPosY(),
              (int) (spikes[i].getSize() * scale),
              (int) (spikes[i].getSize() * scale), this);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void drawFoods(Graphics2D g2) {
    for (int i = 0; i < foods.length; i++) {
      if (foods[i] != null) {
        if (foods[i].getPos().getPosX() < clientManager.getPlayerMe().getPos().getPosX() + (WIDTH / 1.8)
            && foods[i].getPos().getPosX() > clientManager.getPlayerMe().getPos().getPosX() - (WIDTH / 1.8)
            && foods[i].getPos().getPosY() < clientManager.getPlayerMe().getPos().getPosY() + (HEIGHT / 1.8)
            && foods[i].getPos().getPosY() > clientManager.getPlayerMe().getPos().getPosY() - (HEIGHT / 1.8)) {
          g2.setColor(foods[i].getColor());
          g2.fill(new Ellipse2D.Double(foods[i].getPos().getPosX(), foods[i].getPos().getPosY(),
              20 * scale, 20 * scale));
        }
      }
    }
  }

  private void drawPlayers(Graphics2D g2) {
    for (int i = 0; i < otherPlayers.length; i++) {
      if (!otherPlayers[i].getUsername().equals(clientManager.getPlayerMe().getUsername())
          && !otherPlayers[i].isDead()) {
        if (otherPlayers[i].getPos().getPosX() < clientManager.getPlayerMe().getPos().getPosX() + (WIDTH / 1.8)
            && otherPlayers[i].getPos().getPosX() > clientManager.getPlayerMe().getPos().getPosX() - (WIDTH / 1.8)
            && otherPlayers[i].getPos().getPosY() < clientManager.getPlayerMe().getPos().getPosY() + (HEIGHT / 1.8)
            && otherPlayers[i].getPos().getPosY() > clientManager.getPlayerMe().getPos().getPosY() - (HEIGHT / 1.8)) {
          g2.setColor(otherPlayers[i].getColor());
          g2.fill(new Ellipse2D.Double(otherPlayers[i].getPos().getPosX(), otherPlayers[i].getPos().getPosY(),
              otherPlayers[i].getMass() * scale, otherPlayers[i].getMass() * scale));
        }
      }
    }
  }

  private void drawMap(Graphics2D g2) {
    for (int i = 0; i < 10000; i += 40) {
      g2.drawRect(0, i, 10000, 1);
      g2.setColor(new Color(210, 210, 210));
    }

    for (int i = 0; i < 10000; i += 40) {
      g2.drawRect(i, 0, 1, 10000);
      g2.setColor(new Color(210, 210, 210));
    }

    // top (this doesn't work don't know why but it's needed to be there)
    g2.drawRect(0, 1000, 10000, 1);
    g2.setColor(new Color(255, 0, 0));
    g2.setStroke(new BasicStroke(4.0f,
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND,
        10.0f, null, 0.0f));

    // top
    g2.drawRect(0, 1000, 10000, 1);
    g2.setColor(new Color(255, 0, 0));
    g2.setStroke(new BasicStroke(4.0f,
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND,
        10.0f, null, 0.0f));

    // bottom
    g2.drawRect(0, 9000, 10000, 1);
    g2.setColor(new Color(255, 0, 0));
    g2.setStroke(new BasicStroke(4.0f,
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND,
        10.0f, null, 0.0f));

    // left
    g2.drawRect(1000, 0, 1, 10000);
    g2.setColor(new Color(255, 0, 0));
    g2.setStroke(new BasicStroke(4.0f,
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND,
        10.0f, null, 0.0f));

    // right
    g2.drawRect(9000, 0, 1, 10000);
    g2.setColor(new Color(255, 0, 0));
    g2.setStroke(new BasicStroke(4.0f,
        BasicStroke.CAP_ROUND,
        BasicStroke.JOIN_ROUND,
        10.0f, null, 0.0f));
  }

  private void drawPlayerMe(Graphics2D g2) {
    g2.setColor(clientManager.getPlayerMe().getColor());
    g2.fill(new Ellipse2D.Double(clientManager.getPlayerMe().getPos().getPosX(),
        clientManager.getPlayerMe().getPos().getPosY(), clientManager.getPlayerMe().getMass() * scale,
        clientManager.getPlayerMe().getMass() * scale));
  }

  private void updateViewPosition() {
    Point view = new Point(
        (int) clientManager.getPlayerMe().getPos().getPosX() - (WIDTH / 2)
            + ((int) (clientManager.getPlayerMe().getMass() * scale) / 2),
        (int) clientManager.getPlayerMe().getPos().getPosY() - (HEIGHT / 2)
            + ((int) (clientManager.getPlayerMe().getMass() * scale) / 2));
    vPort.setViewPosition(view);
  }

  private double calculateScale() {
    return 1;
  }

  private void recivePacketsFromServer() {
    (new Thread() {
      public void run() {
        while (true) {
          try {
            if (clientManager.getConnected() == ConnectionStates.CONNECTED) {
              Object o = clientManager.getInput().readObject();

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
                  if (p.getUsername().equals(clientManager.getPlayerMe().getUsername())) {
                    clientManager.getPlayerMe().setMass(p.getMass());
                    clientManager.getPlayerMe().setDead(p.isDead());

                    scale = calculateScale();
                  }
                }

                okSendPck = true;
              }
            }

            Thread.sleep(1000 / 60); // 17pps (packets-per-second)
          } catch (Exception e) {
            // e.printStackTrace();
          }
        }
      }
    }).start();
  }
}