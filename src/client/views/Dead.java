package client.views;

import server.entities.Player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import client.Main;
import client.managers.ClientManager;
import client.managers.ClientManager.WindowStates;

public class Dead extends JPanel implements ActionListener {
  private JButton btRiGioca;
  private JButton btHome;

  private ClientManager clientManager;

  public Dead(ClientManager clientManager) {
    this.clientManager = clientManager;

    Dimension newSize = new Dimension(1280, 768);
    setSize(newSize);
    setLayout(null);

    btRiGioca = new JButton("");
    btRiGioca.setBounds(381, 573, 228, 80);
    add(btRiGioca);

    btHome = new JButton("");
    btHome.setBounds(632, 573, 228, 80);
    add(btHome);

    btHome.addActionListener(this);
    btRiGioca.addActionListener(this);
    repaint();
    revalidate();
    setVisible(true);
  }

  public void paintComponent(Graphics g) {
    super.paintComponents(g);
    setFocusable(true);
    requestFocusInWindow();
    Graphics2D g2 = (Graphics2D) g;

    try {
      BufferedImage image = ImageIO.read(Main.class.getResource("imgs/morte.png"));
      g2.drawImage(image, 0, 0, 1280, 768, this);

      g2.setFont(new Font("Arial", Font.PLAIN, 20));
      g2.setColor(new Color(0, 0, 0));
      g2.drawString(clientManager.getPlayerMe().getMass() + "", 668, 417);

      g2.dispose();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(btRiGioca)) {
      clientManager.setPlayerMe(
          new Player(null, clientManager.getPlayerMe().getColor(), clientManager.getPlayerMe().getUsername()));
      clientManager.setWindowStates(WindowStates.PLAY);
    }

    if (e.getSource().equals(btHome)) {
      clientManager.setWindowStates(WindowStates.MENU);

    }
  }

}
