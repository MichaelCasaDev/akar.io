package client.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import client.ClientManager;
import client.ClientManager.WindowStates;
import server.entities.Player;

import javax.swing.JButton;
import javax.swing.JColorChooser;

public class Home extends JPanel implements ActionListener, KeyListener {
  private JTextField textField;
  private JButton btGioca;
  private JButton btColor;
  private Color newColor = new Color(0, 0, 0);
  private String msg = "Username";

  private ClientManager clientManager;

  /**
   * Create the panel.
   */
  public Home(ClientManager clientManager) {
    this.clientManager = clientManager;

    Dimension newSize = new Dimension(1280, 768);
    setSize(newSize);
    setLayout(null);
    repaint();

    textField = new JTextField();
    textField.setBounds(524, 355, 165, 58);
    add(textField);
    textField.setColumns(10);

    btGioca = new JButton("");
    btGioca.setBounds(557, 491, 165, 58);
    add(btGioca);

    btColor = new JButton("");
    btColor.setBounds(701, 357, 55, 55);
    add(btColor);

    revalidate();
    btGioca.addActionListener(this);
    textField.addActionListener(this);
    btColor.addActionListener(this);

    textField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        warn();
      }

      public void removeUpdate(DocumentEvent e) {
        warn();
      }

      public void insertUpdate(DocumentEvent e) {
        warn();
      }

      public void warn() {
        if (textField.getText().length() < 12) {
          msg = textField.getText();
        }
      }
    });

    setVisible(true);
  }

  public void paintComponent(Graphics g) {
    super.paintComponents(g);
    setFocusable(true);

    Graphics2D g2 = (Graphics2D) g;

    try {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      File img = new File("src/client/home.png");
      BufferedImage image = ImageIO.read(img);
      g2.drawImage(image, 0, 0, 1280, 768, this);
      g2.drawString(msg, 560, 389);
      g2.setColor(newColor);
      g2.fill(new Ellipse2D.Double(708.5, 364.5, 40, 40));
      g2.dispose();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(btGioca)) {
      System.out.println("colore: " + newColor + " username: " + msg);

      clientManager.setPlayerMe(new Player(null, newColor, msg));
      clientManager.setWindowStates(WindowStates.PLAY);
    }
    if (e.getSource().equals(btColor)) {
      newColor = JColorChooser.showDialog(null, "Choose a color", Color.RED);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
